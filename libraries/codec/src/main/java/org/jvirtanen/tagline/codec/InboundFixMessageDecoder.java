/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * An inbound FIX message decoder.
 */
public class InboundFixMessageDecoder extends ByteToMessageDecoder {

    private final int maxBodyLength;

    private final FixVersionDecoder versionDecoder;
    private final FixBodyLengthDecoder bodyLengthDecoder;
    private final FixMessageFinder messageFinder;

    /**
     * Construct a new instance using the default configuration.
     */
    public InboundFixMessageDecoder() {
        this(InboundFixMessageDecoderConfig.DEFAULTS);
    }

    /**
     * Construct a new instance.
     *
     * @param config the configuration
     */
    public InboundFixMessageDecoder(final InboundFixMessageDecoderConfig config) {
        maxBodyLength = config.maxBodyLength();

        versionDecoder = new FixVersionDecoder();
        bodyLengthDecoder = new FixBodyLengthDecoder();
        messageFinder = new FixMessageFinder();
    }

    /**
     * Decode inbound FIX messages.
     *
     * @param ctx the context
     * @param in the input
     * @param out the output
     * @throws TooLongInboundFixMessageException if the BodyLength(9) value in
     *     an inbound FIX message exceeds the maximum BodyLength(9) value
     */
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        int readableBytes = in.readableBytes();

        if (readableBytes < 2)
            return;

        int startIndex = in.readerIndex();

        if (in.getShort(startIndex) != BEGIN_STRING_SHORT) {
            decodeGarbled(in, out);

            return;
        }

        int beginStringValueIndex = startIndex + 2;

        try {
            int bodyLengthIndex = versionDecoder.decode(in, beginStringValueIndex, startIndex + readableBytes - beginStringValueIndex);
            if (bodyLengthIndex < 0)
                return;

            int bodyLengthValueIndex = bodyLengthIndex + 2;

            if (readableBytes < bodyLengthValueIndex - startIndex)
                return;

            if (in.getShort(bodyLengthIndex) != BODY_LENGTH_SHORT) {
                decodeGarbled(in, out);

                return;
            }

            int bodyIndex = bodyLengthDecoder.decode(in, bodyLengthValueIndex, startIndex + readableBytes - bodyLengthValueIndex);
            if (bodyIndex < 0)
                return;

            int bodyLength = bodyLengthDecoder.bodyLength();
            if (bodyLength > maxBodyLength)
                tooLongInboundFixMessage(in);

            int trailerIndex = bodyIndex + bodyLength;
            int checkSumValueIndex = trailerIndex + 3;
            int endIndex = trailerIndex + 7;

            if (readableBytes < endIndex - startIndex)
                return;

            if (in.getMedium(trailerIndex) != CHECK_SUM_MEDIUM) {
                decodeGarbled(in, out);

                return;
            }

            var version = versionDecoder.version();
            var content = in.retainedSlice(startIndex, trailerIndex - startIndex);
            int bodyOffset = bodyIndex - startIndex;
            int checkSum = FixCheckSumDecoder.decode(in, checkSumValueIndex);

            var message = new DefaultInboundFixMessage(version, content, bodyOffset, checkSum);

            in.readerIndex(endIndex);
            out.add(message);
        } catch (FixDecoderException e) {
            decodeGarbled(in, out);
        }
    }

    private void decodeGarbled(final ByteBuf in, final List<Object> out) {
        int startIndex = in.readerIndex();

        int endIndex = messageFinder.find(in, startIndex, in.readableBytes());
        if (endIndex < 0)
            return;

        var content = in.retainedSlice(startIndex, endIndex - startIndex);

        var garbledMessage = new DefaultInboundFixMessage(content);

        in.readerIndex(endIndex);
        out.add(garbledMessage);
    }

    private static void tooLongInboundFixMessage(final ByteBuf in) {
        in.readerIndex(in.readerIndex() + in.readableBytes());

        throw new TooLongInboundFixMessageException();
    }

}
