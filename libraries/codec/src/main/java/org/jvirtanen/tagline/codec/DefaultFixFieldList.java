/*
 * Copyright 2025 Jussi Virtanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

/**
 * The default implementation of a FIX field list.
 */
public class DefaultFixFieldList implements FixFieldList {

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
    public DefaultFixFieldList() {
        this(FixFieldListConfig.DEFAULTS);
    }

    /**
     * Construct a new instance.
     *
     * @param config the configuration
     */
    public DefaultFixFieldList(final FixFieldListConfig config) {
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

    @Override
    public void decode(final InboundFixMessage message) {
        decode(message.content());
    }

    @Override
    public void decode(final ByteBuf buffer) {
        decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    @Override
    public void decode(final ByteBuf buffer, final int offset, final int length) {
        enlargeBuffer(length);
        fillBuffer(buffer, offset, length);
        decodeBuffer(length);
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

    @Override
    public void clear() {
        size = 0;

        for (var value : values)
            value.clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int tagAt(final int index) {
        if (index >= size)
            indexOutOfBounds();

        return tags[index];
    }

    @Override
    public FixValue valueAt(final int index) {
        if (index >= size)
            indexOutOfBounds();

        return values[index];
    }

    @Override
    public int indexOf(final int tag) {
        return indexOf(tag, 0);
    }

    @Override
    public int indexOf(final int tag, final int fromIndex) {
        for (int index = fromIndex; index < size; index++) {
            if (tags[index] != tag)
                continue;

            return index;
        }

        return -1;
    }

    @Override
    public FixValue valueOf(final int tag) {
        return valueOf(tag, 0);
    }

    @Override
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
        public byte byteAt(final int index) {
            if (index < 0 || index >= length)
                indexOutOfBounds();

            return bytes[offset + index];
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
