/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * A FIX value.
 */
public interface FixValue extends CharSequence {

    /**
     * Get the length.
     *
     * @return the length
     */
    int length();

    /**
     * Get the byte at the specified index.
     *
     * @param index the index
     * @return the byte at the index
     * @throws IndexOutOfBoundsException if the index is smaller than 0 or
     *     equal to or greater than the length
     */
    byte byteAt(int index);

    /**
     * Get the character at the specified index.
     *
     * @param index the index
     * @return the character at the index
     * @throws IndexOutOfBoundsException if the index is smaller than 0 or
     *     equal to or greater than the length
     */
    char charAt(int index);

    /**
     * Get this value as a Boolean.
     *
     * @return this value as a Boolean
     * @throws FixDecoderException if this value is not a Boolean
     */
    boolean asBoolean();

    /**
     * Get this value as a Char.
     *
     * @return this value as a Char
     * @throws FixDecoderException if this value is not a Char
     */
    char asChar();

    /**
     * Get this value as an Int.
     *
     * @return this value as an Int
     * @throws FixDecoderException if this value is not an Int or cannot be
     *     represented as a {@code long}
     */
    long asInt();

    /**
     * Get this value as a Float.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a Float or cannot be
     *     represented as a {@link FixFloat}
     */
    void asFloat(FixFloat container);

    /**
     * Get this value as a Float.
     *
     * @return this value as a Float
     * @throws FixDecoderException if this value is not a Float or cannot be
     *     represented as a {@link FixFloat}
     */
    double asFloat();

    /**
     * Get this value as a String.
     *
     * @return this value as a String
     */
    CharSequence asString();

    /**
     * Get this value as a UTCDate or LocalMktDate.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a UTCDate or
     *     LocalMktDate
     */
    void asDate(FixDate container);

    /**
     * Get this value as a UTCTimeOnly.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a UTCTimeOnly
     */
    void asTime(FixTime container);

    /**
     * Get this value as a UTCTimestamp.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a UTCTimestamp
     */
    void asTimestamp(FixTimestamp container);

    /**
     * Get this value as a UTCTimestamp.
     *
     * @return this value as number of milliseconds from the epoch of
     *     1970-01-01T00:00:00.000Z
     * @throws FixDecoderException if this value is not a UTCTimestamp
     */
    long asTimestamp();

    /**
     * Get this value as raw bytes. The value container can be longer than the
     * value.
     *
     * @param container a value container
     * @throws IndexOutOfBoundsException if the length of the value container is
     *     less than the length of this value
     * @see #length()
     */
    void asBytes(byte[] container);

    /**
     * Get a subsequence from this value.
     *
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @throws IndexOutOfBoundsException if the start index is smaller than
     *     zero, the start index is greater than the end index, or the end
     *     index is greater than the length
     */
    CharSequence subSequence(int start, int end);

    /**
     * Get a string representation of this value.
     *
     * @return a string representation of this value
     */
    String toString();

}
