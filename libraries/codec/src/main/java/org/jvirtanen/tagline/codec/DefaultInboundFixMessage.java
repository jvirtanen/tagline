/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;

/**
 * The default implementation of an inbound FIX message.
 */
public class DefaultInboundFixMessage extends DefaultFixMessage implements InboundFixMessage {

    private final boolean isGarbled;
    private final int bodyOffset;
    private final int bodyLength;
    private final int checkSum;

    /**
     * Construct a new non-garbled instance.
     *
     * @param version the version
     * @param content the content
     * @param bodyOffset the body offset in the content
     * @param bodyLength the BodyLength(9) value
     * @param checkSum the CheckSum(10) value
     */
    public DefaultInboundFixMessage(final FixVersion version, final ByteBuf content,
            final int bodyOffset, final int bodyLength, final int checkSum) {
        this(false, version, content, bodyOffset, bodyLength, checkSum);
    }

    /**
     * Construct a new garbled instance.
     *
     * @param content the content
     */
    public DefaultInboundFixMessage(final ByteBuf content) {
        this(true, null, content, -1, -1, -1);
    }

    private DefaultInboundFixMessage(final boolean isGarbled, final FixVersion version,
            final ByteBuf content, final int bodyOffset, final int bodyLength, final int checkSum) {
        super(version, content);

        this.isGarbled = isGarbled;
        this.bodyOffset = bodyOffset;
        this.bodyLength = bodyLength;
        this.checkSum = checkSum;
    }

    @Override
    public boolean isGarbled() {
        return isGarbled;
    }

    @Override
    public int bodyOffset() {
        return bodyOffset;
    }

    @Override
    public int bodyLength() {
        return bodyLength;
    }

    @Override
    public int checkSum() {
        return checkSum;
    }

    @Override
    public DefaultInboundFixMessage copy() {
        return replace(content.copy());
    }

    @Override
    public DefaultInboundFixMessage duplicate() {
        return replace(content.duplicate());
    }

    @Override
    public DefaultInboundFixMessage retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    @Override
    public DefaultInboundFixMessage replace(final ByteBuf content) {
        return new DefaultInboundFixMessage(version(), content, bodyOffset, bodyLength, checkSum);
    }

    @Override
    public DefaultInboundFixMessage retain() {
        super.retain();

        return this;
    }

    @Override
    public DefaultInboundFixMessage retain(final int increment) {
        super.retain(increment);

        return this;
    }

    @Override
    public DefaultInboundFixMessage touch() {
        super.touch();

        return this;
    }

    @Override
    public DefaultInboundFixMessage touch(final Object hint) {
        super.touch(hint);

        return this;
    }

}
