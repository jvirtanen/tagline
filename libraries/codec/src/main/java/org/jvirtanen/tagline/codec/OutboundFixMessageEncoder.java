/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/**
 * An outbound FIX message encoder.
 */
public class OutboundFixMessageEncoder extends MessageToMessageEncoder<OutboundFixMessage> {

    private final FixCheckSumCalculator checkSumCalculator;

    /**
     * Construct a new instance.
     */
    public OutboundFixMessageEncoder() {
        checkSumCalculator = new FixCheckSumCalculator();
    }

    /**
     * Encode an outbound FIX message.
     *
     * @param ctx the context
     * @param msg the message
     * @param out the output
     * @throws IllegalArgumentException if the length of the message content
     *     is greater than 9999999 bytes
     */
    @Override
    protected void encode(final ChannelHandlerContext ctx, final OutboundFixMessage msg, final List<Object> out) {
        var header = encodeHeader(ctx.alloc(), msg);
        var body = msg.content().retain();

        encodeTrailer(header, body);

        out.add(header);
        out.add(body);
    }

    private ByteBuf encodeHeader(final ByteBufAllocator allocator, final OutboundFixMessage msg) {
        var version = msg.version();

        var header = allocator.ioBuffer(2 + version.length() + 2 + FixMediumEncoder.MAX_LENGTH);

        header.writeShort(BEGIN_STRING_SHORT);
        version.encode(header);
        header.writeShort(BODY_LENGTH_SHORT);
        FixMediumEncoder.encode(msg.content().readableBytes(), SOH, header);

        return header;
    }

    private void encodeTrailer(final ByteBuf header, final ByteBuf body) {
        int checkSum = checkSumCalculator.calculate(header) + checkSumCalculator.calculate(body);

        body.writeMedium(CHECK_SUM_MEDIUM);
        FixCheckSumEncoder.encode(checkSum & FixCheckSumCalculator.MASK, body);
    }

}
