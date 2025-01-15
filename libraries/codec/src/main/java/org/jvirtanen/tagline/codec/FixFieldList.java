/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;

/**
 * A FIX field list.
 */
public interface FixFieldList {

    /**
     * Decode a message.
     *
     * @param message a message
     * @throws FixDecoderException if the message cannot be decoded as FIX
     *     fields
     */
    void decode(InboundFixMessage message);

    /**
     * Decode the readable bytes in a buffer.
     *
     * @param buffer a buffer
     * @throws FixDecoderException if the bytes cannot be decoded as FIX
     *     fields
     */
    void decode(ByteBuf buffer);

    /**
     * Decode bytes in a buffer.
     *
     * @param buffer a buffer
     * @param offset the offset
     * @param length the length
     * @throws FixDecoderException if the bytes cannot be decoded as FIX
     *     fields
     */
    void decode(ByteBuf buffer, int offset, int length);

    /**
     * Clear this instance.
     */
    void clear();

    /**
     * Get the number of fields.
     *
     * @return the number of fields
     */
    int size();

    /**
     * Get the tag at the specified index.
     *
     * @param index the index
     * @return the tag at the specified index
     * @throws IndexOutOfBoundsException if the index is less than 0 or equal
     *     to or greater than the number of fields
     */
    int tagAt(int index);

    /**
     * Get the value at the specified index.
     *
     * @param index the index
     * @return the value at the specified index
     * @throws IndexOutOfBoundsException if the index is less than 0 or equal
     *     to or greater than the number of fields
     */
    FixValue valueAt(int index);

    /**
     * Get the index of the first occurrence of the specified tag or
     * {@code -1} if the tag does not occur.
     *
     * @param tag a tag
     * @return the index of the first occurrence of the specified tag or
     *     {@code -1} if the tag does not occur
     */
    int indexOf(int tag);

    /**
     * Get the index of the next occurrence of the specified tag, starting
     * from the specified index, or {@code -1} if there are no more occurrences
     * of the tag.
     *
     * @param tag a tag
     * @param fromIndex the index to start from
     * @return the index of the next occurrence of the specified tag, starting
     *     from the specified index, or {@code -1} if there are no more
     *     occurrences of the tag
     * @throws IndexOutOfBoundsException if the index to start from is less
     *     than zero
     */
    int indexOf(int tag, int fromIndex);

    /**
     * Get the value of the first occurrence of the specified tag or
     * {@code null} if the tag does not occur.
     *
     * @param tag a tag
     * @return the value of the first occurrence of the specified tag or
     *     {@code null} if the tag does not occur
     */
    FixValue valueOf(int tag);

    /**
     * Get the value of the next occurrence of the specified tag, starting
     * from the specified index, or {@code null} if there are no more
     * occurrences of the tag.
     *
     * @param tag a tag
     * @param fromIndex the index to start from
     * @return the value of the next occurrence of the specified tag, starting
     *     from the specified index, or {@code null} if there are no more
     *     occurrences of the tag
     * @throws IndexOutOfBoundsException if the index to start from is less
     *     than zero
     */
    FixValue valueOf(int tag, int fromIndex);

}
