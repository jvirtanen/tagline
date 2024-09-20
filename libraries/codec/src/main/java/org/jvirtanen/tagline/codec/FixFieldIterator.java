/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A FIX field iterator.
 */
public class FixFieldIterator implements Iterable<FixField>, Iterator<FixField> {

    private final FixTagDecoder tag;
    private final FixValueDecoder value;

    private final DefaultFixFloat floatContainer;
    private final DefaultFixTimestamp timestampContainer;

    private final Element element;

    private ByteBuf buffer;

    private int startIndex;
    private int currentIndex;
    private int endIndex;

    /**
     * Construct a new instance.
     */
    public FixFieldIterator() {
        tag = new FixTagDecoder();
        value = new FixValueDecoder();

        floatContainer = new DefaultFixFloat();
        timestampContainer = new DefaultFixTimestamp();

        element = new Element();

        buffer = null;

        startIndex = 0;
        currentIndex = 0;
        endIndex = 0;
    }

    /**
     * Iterate the fields in an inbound message.
     *
     * @param message a message
     * @return this instance
     */
    public FixFieldIterator iterate(final InboundFixMessage message) {
        return iterate(message.content(), message.bodyOffset(), message.bodyLength());
    }

    /**
     * Iterate the fields in a buffer.
     *
     * @param buffer a buffer
     * @param offset the offset of the region to read from
     * @param length the length of the region to read from
     * @return this instance
     */
    public FixFieldIterator iterate(final ByteBuf buffer, final int offset, final int length) {
        this.buffer = buffer;

        startIndex = offset;
        currentIndex = offset;
        endIndex = offset + length;

        return this;
    }

    /**
     * Return this instance.
     *
     * @return this instance
     */
    @Override
    public FixFieldIterator iterator() {
        return this;
    }

    /**
     * Returns true if there are more fields, otherwise returns false.
     *
     * @return true if there are more fields, otherwise false
     */
    @Override
    public boolean hasNext() {
        return currentIndex < endIndex;
    }

    /**
     * Return the next field.
     *
     * @return the next field
     * @throws FixDecoderException if decoding a field fails
     * @throws NoSuchElementException if there are no more fields
     */
    @Override
    public FixField next() {
        if (currentIndex == endIndex)
            noSuchElement();

        currentIndex = tag.decode(buffer, currentIndex, endIndex - currentIndex);
        if (currentIndex < 0)
            incompleteField();

        currentIndex = value.decode(buffer, currentIndex, endIndex - currentIndex);
        if (currentIndex < 0)
            incompleteField();

        return element;
    }

    private static void noSuchElement() {
        throw new NoSuchElementException();
    }

    private static void incompleteField() {
        throw new FixDecoderException("Incomplete field");
    }

    private static void indexOutOfBounds() {
        throw new IndexOutOfBoundsException();
    }

    private class Element implements FixField {

        @Override
        public int tag() {
            return tag.tag();
        }

        @Override
        public int length() {
            return value.length();
        }

        @Override
        public char charAt(final int index) {
            if (index >= value.length())
                indexOutOfBounds();

            return FixStringDecoder.charAt(value.bytes(), index, value.length());
        }

        @Override
        public boolean asBoolean() {
            return FixBooleanDecoder.decode(value.bytes(), value.length());
        }

        @Override
        public char asChar() {
            return FixCharDecoder.decode(value.bytes(), value.length());
        }

        @Override
        public long asInt() {
            return FixIntDecoder.decode(value.bytes(), value.length());
        }

        @Override
        public void asFloat(final FixFloat container) {
            FixFloatDecoder.decode(value.bytes(), value.length(), container);
        }

        @Override
        public double asFloat() {
            FixFloatDecoder.decode(value.bytes(), value.length(), floatContainer);

            return floatContainer.doubleValue();
        }

        @Override
        public CharSequence asString() {
            return this;
        }

        @Override
        public void asDate(final FixDate container) {
            FixDateDecoder.decode(value.bytes(), value.length(), container);
        }

        @Override
        public void asTime(final FixTime container) {
            FixTimeDecoder.decode(value.bytes(), value.length(), container);
        }

        @Override
        public void asTimestamp(final FixTimestamp container) {
            FixTimestampDecoder.decode(value.bytes(), value.length(), container);
        }

        @Override
        public long asTimestamp() {
            FixTimestampDecoder.decode(value.bytes(), value.length(), timestampContainer);

            return timestampContainer.getEpochMilli();
        }

        @Override
        public byte[] asBytes() {
            return value.bytes();
        }

        @Override
        public CharSequence subSequence(final int start, final int end) {
            return FixStringDecoder.subSequence(start, end, value.bytes(), value.length());
        }

        @Override
        public String toString() {
            return FixStringDecoder.toString(value.bytes(), value.length());
        }

    }

}
