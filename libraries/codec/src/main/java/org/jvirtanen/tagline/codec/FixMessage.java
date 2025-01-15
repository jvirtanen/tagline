/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

/**
 * A FIX message.
 */
public interface FixMessage extends ByteBufHolder {

    /**
     * Get the protocol version.
     *
     * @return the protocol version
     */
    FixVersion version();

    /**
     * Get the content.
     *
     * @return the content
     */
    @Override
    ByteBuf content();

    /**
     * Get the reference count.
     *
     * @return the reference count
     */
    @Override
    int refCnt();

    /**
     * Copy this instance.
     *
     * @return a copy of this instance
     */
    @Override
    FixMessage copy();

    /**
     * Duplicate this instance.
     *
     * @return a duplicate of this instance
     */
    @Override
    FixMessage duplicate();

    /**
     * Duplicate this instance.
     *
     * @return a retained duplicate of this instance
     */
    @Override
    FixMessage retainedDuplicate();

    /**
     * Create a new instance with the specified content.
     *
     * @param content the content
     * @return a new instance
     */
    @Override
    FixMessage replace(ByteBuf content);

    /**
     * Increase the reference count by 1.
     *
     * @return this instance
     */
    @Override
    FixMessage retain();

    /**
     * Increase the reference count by the specified increment.
     *
     * @param increment the increment
     * @return this instance
     */
    @Override
    FixMessage retain(int increment);

    /**
     * Record the current access location for debugging purposes.
     *
     * @return this instance
     */
    @Override
    FixMessage touch();

    /**
     * Decrease the reference count by 1.
     *
     * @return true if the reference count became 0 and this instance was
     *     deallocated, otherwise false
     */
    @Override
    boolean release();

    /**
     * Decrease the reference count by the specified decrement.
     *
     * @param decrement the decrement
     * @return true if the reference count became 0 and this instance was
     *     deallocated, otherwise false
     */
    @Override
    boolean release(int decrement);

    /**
     * Record the current access location for debugging purposes.
     *
     * @param hint the hint
     * @return this instance
     */
    @Override
    FixMessage touch(Object hint);

}
