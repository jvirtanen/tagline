/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import java.util.List;

/**
 * <p>A FIX field list decoder.</p>
 *
 * <p>The {@link FixFieldList} instances produced by this decoder use reference
 * counting and resource pooling. Use {@link ReferenceCountUtil#retain(Object)}
 * and {@link ReferenceCountUtil#release(Object)} to manage the reference count
 * manually, or use, for example, {@link SimpleChannelInboundHandler} that
 * automatically decrements the reference count.</p>
 *
 * <p>Note that this handler is stateful so each channel must have their own
 * instance.</p>
 */
public class FixFieldListDecoder extends MessageToMessageDecoder<InboundFixMessage> {

    private final FixVersion version;
    private final FixCheckSumCalculator checkSum;
    private final FixFieldListPool pool;

    /**
     * Construct a new instance using the default configuration.
     */
    public FixFieldListDecoder() {
        this(FixFieldListDecoderConfig.DEFAULTS);
    }

    /**
     * Construct a new instance.
     *
     * @param config the configuration
     */
    public FixFieldListDecoder(final FixFieldListDecoderConfig config) {
        version = config.version();
        checkSum = config.isCheckSumEnabled() ? new FixCheckSumCalculator() : null;
        pool = new FixFieldListPool(config.messageConfig());
    }

    /**
     * Decode FIX field lists.
     *
     * @param ctx the context
     * @param msg an inbound FIX message
     * @param out the output
     * @throws GarbledFixMessageException if the inbound FIX message is garbled
     */
    @Override
    protected void decode(final ChannelHandlerContext ctx, final InboundFixMessage msg, final List<Object> out) {
        checkGarbled(msg);
        checkVersion(msg);
        checkCheckSum(msg);

        decode(msg, out);
    }

    private void decode(final InboundFixMessage msg, final List<Object> out) {
        var fields = pool.get();

        try {
            fields.decode(msg);

            out.add(fields);
        } catch (Exception e) {
            fields.release();

            garbled(e);
        }
    }

    private void checkGarbled(final InboundFixMessage msg) {
        if (!msg.isGarbled())
            return;

        garbled("Invalid message");
    }

    private static void garbled(final String message) {
        throw new GarbledFixMessageException(message);
    }

    private static void garbled(final Throwable cause) {
        throw new GarbledFixMessageException(cause);
    }

    private void checkVersion(final InboundFixMessage msg) {
        if (version == null)
            return;

        var actual = msg.version();

        if (version.equals(actual))
            return;

        invalidVersion(actual, version);
    }

    private static void invalidVersion(final FixVersion actual, final FixVersion expected) {
        garbled("Unexpected BeginString(8) value \"" + actual + "\", expected \"" + expected + "\"");
    }

    private void checkCheckSum(final InboundFixMessage msg) {
        if (checkSum == null)
            return;

        int actual = msg.checkSum();
        int expected = checkSum.calculate(msg);

        if (actual == expected)
            return;

        invalidCheckSum(actual, expected);
    }

    private static void invalidCheckSum(final int actual, final int expected) {
        garbled("Unexpected CheckSum(10) value " + actual + ", expected " + expected);
    }

}
