/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

/**
 * <p>A FIX field list.</p>
 *
 * <p>This class implements efficient read-only random access into a linear
 * sequence of zero or more fields. Each field consists of a tag and a value.
 * A FIX field list parses tags eagerly when decoding input and values only
 * lazily on demand.</p>
 */
public class FixFieldList {

    private final FixTagDecoder tagDecoder;

    private final DefaultFixFloat floatContainer;
    private final DefaultFixTimestamp timestampContainer;

    private byte[] bytes;

    private int size;

    private int[] tags;
    private Value[] values;

    /**
     * Construct a new instance using the default configuration.
     */
    public FixFieldList() {
        this(FixFieldListConfig.DEFAULTS);
    }

    /**
     * Construct a new instance.
     *
     * @param config the configuration
     */
    public FixFieldList(final FixFieldListConfig config) {
        tagDecoder = new FixTagDecoder();

        floatContainer = new DefaultFixFloat();
        timestampContainer = new DefaultFixTimestamp();

        bytes = new byte[config.initialBufferSize()];

        size = 0;

        int initialCapacity = config.initialCapacity();

        tags = new int[initialCapacity];
        values = new Value[initialCapacity];

        for (int i = 0; i < initialCapacity; i++)
            this.values[i] = new Value();
    }

    /**
     * Decode a message.
     *
     * @param message a message
     * @return this instance
     * @throws FixDecoderException if the message cannot be decoded as FIX
     *     fields
     */
    public FixFieldList decode(final InboundFixMessage message) {
        return decode(message.content());
    }

    /**
     * Decode the readable bytes in a buffer.
     *
     * @param buffer a buffer
     * @return this instance
     * @throws FixDecoderException if the bytes cannot be decoded as FIX
     *     fields
     */
    public FixFieldList decode(final ByteBuf buffer) {
        return decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    /**
     * Decode bytes in a buffer.
     *
     * @param buffer a buffer
     * @param offset the offset
     * @param length the length
     * @return this instance
     * @throws FixDecoderException if the bytes cannot be decoded as FIX
     *     fields
     */
    public FixFieldList decode(final ByteBuf buffer, final int offset, final int length) {
        enlargeBuffer(length);
        fillBuffer(buffer, offset, length);
        decodeBuffer(length);

        return this;
    }

    private void enlargeBuffer(final int length) {
        if (bytes.length >= length)
            return;

        bytes = new byte[MathUtil.findNextPositivePowerOfTwo(length)];
    }

    private void fillBuffer(final ByteBuf buffer, final int offset, final int length) {
        buffer.getBytes(offset, bytes, 0, length);
    }

    private void decodeBuffer(final int length) {
        int capacity = tags.length;

        size = 0;

        int offset = 0;

        while (offset < length) {
            if (size == capacity)
                increaseCapacity();

            offset = tagDecoder.decode(bytes, offset, length);
            if (offset < 0)
                incompleteField();

            tags[size] = tagDecoder.tag();

            var value = values[size];

            value.offset = offset;

            offset = FixValueDecoder.decode(bytes, offset, length);
            if (offset < 0)
                incompleteField();

            value.length = offset - value.offset - 1;

            size++;
        }
    }

    private void increaseCapacity() {
        int oldCapacity = tags.length;
        int newCapacity = 2 * oldCapacity;

        tags = Arrays.copyOf(tags, newCapacity);
        values = Arrays.copyOf(values, newCapacity);

        for (int i = oldCapacity; i < newCapacity; i++)
            values[i] = new Value();
    }

    /**
     * Clear this instance.
     */
    public void clear() {
        size = 0;

        for (var value : values)
            value.clear();
    }

    /**
     * Get the number of fields.
     *
     * @return the number of fields
     */
    public int size() {
        return size;
    }

    /**
     * Get the tag at the specified index.
     *
     * @param index the index
     * @return the tag at the specified index
     * @throws IndexOutOfBoundsException if the index is less than 0 or equal
     *     to or greater than the number of fields
     */
    public int tagAt(final int index) {
        if (index >= size)
            indexOutOfBounds();

        return tags[index];
    }

    /**
     * Get the value at the specified index.
     *
     * @param index the index
     * @return the value at the specified index
     * @throws IndexOutOfBoundsException if the index is less than 0 or equal
     *     to or greater than the number of fields
     */
    public FixValue valueAt(final int index) {
        if (index >= size)
            indexOutOfBounds();

        return values[index];
    }

    /**
     * Get the index of the first occurrence of the specified tag or
     * {@code -1} if the tag does not occur.
     *
     * @param tag a tag
     * @return the index of the first occurrence of the specified tag or
     *     {@code -1} if the tag does not occur
     */
    public int indexOf(final int tag) {
        return indexOf(tag, 0);
    }

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
    public int indexOf(final int tag, final int fromIndex) {
        for (int index = fromIndex; index < size; index++) {
            if (tags[index] != tag)
                continue;

            return index;
        }

        return -1;
    }

    /**
     * Get the value of the first occurrence of the specified tag or
     * {@code null} if the tag does not occur.
     *
     * @param tag a tag
     * @return the value of the first occurrence of the specified tag or
     *     {@code null} if the tag does not occur
     */
    public FixValue valueOf(final int tag) {
        return valueOf(tag, 0);
    }

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
    public FixValue valueOf(final int tag, final int fromIndex) {
        for (int index = fromIndex; index < size; index++) {
            if (tags[index] != tag)
                continue;

            return values[index];
        }

        return null;
    }

    /**
     * Get a string representation of this instance.
     *
     * @return a string representation of this instance
     */
    @Override
    public String toString() {
        var builder = new StringBuilder();

        for (int index = 0; index < size; index++)
            builder
                .append(tags[index])
                .append("=")
                .append(values[index])
                .append("|");

        return builder.toString();
    }

    private class Value implements FixValue {

        int offset;
        int length;

        void clear() {
            offset = 0;
            length = 0;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(final int index) {
            if (index < 0 || index >= length)
                indexOutOfBounds();

            return FixStringDecoder.charAt(bytes, offset + index, length);
        }

        @Override
        public boolean asBoolean() {
            return FixBooleanDecoder.decode(bytes, offset, length);
        }

        @Override
        public char asChar() {
            return FixCharDecoder.decode(bytes, offset, length);
        }

        @Override
        public long asInt() {
            return FixIntDecoder.decode(bytes, offset, length);
        }

        @Override
        public void asFloat(final FixFloat container) {
            FixFloatDecoder.decode(bytes, offset, length, container);
        }

        @Override
        public double asFloat() {
            FixFloatDecoder.decode(bytes, offset, length, floatContainer);

            return floatContainer.doubleValue();
        }

        @Override
        public CharSequence asString() {
            return this;
        }

        @Override
        public void asDate(final FixDate container) {
            FixDateDecoder.decode(bytes, offset, length, container);
        }

        @Override
        public void asTime(final FixTime container) {
            FixTimeDecoder.decode(bytes, offset, length, container);
        }

        @Override
        public void asTimestamp(final FixTimestamp container) {
            FixTimestampDecoder.decode(bytes, offset, length, container);
        }

        @Override
        public long asTimestamp() {
            FixTimestampDecoder.decode(bytes, offset, length, timestampContainer);

            return timestampContainer.getEpochMilli();
        }

        @Override
        public void asBytes(final byte[] container) {
            System.arraycopy(bytes, offset, container, 0, length);
        }

        @Override
        public CharSequence subSequence(final int start, final int end) {
            if (start < 0 || end > length)
                indexOutOfBounds();

            return FixStringDecoder.subSequence(bytes, offset + start, offset + end);
        }

        @Override
        public String toString() {
            return FixStringDecoder.toString(bytes, offset, length);
        }

    }

    private static void indexOutOfBounds() {
        throw new IndexOutOfBoundsException();
    }

    private static void incompleteField() {
        throw new FixDecoderException("Incomplete field");
    }

}
