/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;

abstract class DefaultFixMessage implements FixMessage {

    private final FixVersion version;

    /**
     * The content.
     */
    protected final ByteBuf content;

    DefaultFixMessage(final FixVersion version, final ByteBuf content) {
        this.version = version;
        this.content = content;
    }

    @Override
    public FixVersion version() {
        return version;
    }

    @Override
    public ByteBuf content() {
        return content;
    }

    @Override
    public int refCnt() {
        return content.refCnt();
    }

    @Override
    public DefaultFixMessage copy() {
        return replace(content.copy());
    }

    @Override
    public DefaultFixMessage duplicate() {
        return replace(content.duplicate());
    }

    @Override
    public DefaultFixMessage retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    @Override
    public abstract DefaultFixMessage replace(final ByteBuf content);

    @Override
    public DefaultFixMessage retain() {
        content.retain();

        return this;
    }

    @Override
    public DefaultFixMessage retain(final int increment) {
        content.retain(increment);

        return this;
    }

    @Override
    public boolean release() {
        return content.release();
    }

    @Override
    public boolean release(final int decrement) {
        return content.release(decrement);
    }

    @Override
    public DefaultFixMessage touch() {
        content.touch();

        return this;
    }

    @Override
    public DefaultFixMessage touch(final Object hint) {
        content.touch(hint);

        return this;
    }

}
