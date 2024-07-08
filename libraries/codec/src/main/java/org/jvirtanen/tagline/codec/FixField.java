/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * A FIX field.
 */
public interface FixField extends CharSequence {

    /**
     * Get the tag.
     *
     * @return the tag
     */
    int tag();

    /**
     * Get the length of the value.
     *
     * @return the length of the value
     */
    int length();

    /**
     * Get the character at the specified index in the value.
     *
     * @param index the index
     * @return the character at the index
     * @throws IndexOutOfBoundsException if the index is smaller than 0 or
     *     equal to or greater than the length of the value
     */
    char charAt(int index);

    /**
     * Get the value as a Boolean.
     *
     * @return the value as a Boolean
     * @throws FixDecoderException if the value is not a Boolean
     */
    boolean asBoolean();

    /**
     * Get the value as a Char.
     *
     * @return the value as a Char
     * @throws FixDecoderException if the value is not a Char
     */
    char asChar();

    /**
     * Get the value as an Int.
     *
     * @return the value as an Int
     * @throws FixDecoderException if the value is not an Int or cannot be
     *     represented as a {@code long}
     */
    long asInt();

    /**
     * Get the value as a Float.
     *
     * @param container a value container
     * @throws FixDecoderException if the value is not a Float or cannot be
     *     represented as a {@link FixFloat}
     */
    void asFloat(FixFloat container);

    /**
     * Get the value as a Float.
     *
     * @return the value as a Float
     * @throws FixDecoderException if the value is not a Float or cannot be
     *     represented as a {@link FixFloat}
     */
    double asFloat();

    /**
     * Get the value as a String.
     *
     * @return the value as a String
     */
    CharSequence asString();

    /**
     * Get the value as a UTCDate or LocalMktDate.
     *
     * @param container a value container
     * @throws FixDecoderException if the value is not a UTCDate or
     *     LocalMktDate
     */
    void asDate(FixDate container);

    /**
     * Get the value as a UTCTimeOnly.
     *
     * @param container a value container
     * @throws FixDecoderException if the value is not a UTCTimeOnly
     */
    void asTime(FixTime container);

    /**
     * Get the value as a UTCTimestamp.
     *
     * @param container a value container
     * @throws FixDecoderException if the value is not a UTCTimestamp
     */
    void asTimestamp(FixTimestamp container);

    /**
     * Get the value as a UTCTimestamp.
     *
     * @return the value as number of milliseconds from the epoch of
     *     1970-01-01T00:00:00.000Z
     * @throws FixDecoderException if the value is not a UTCTimestamp
     */
    long asTimestamp();

    /**
     * Get the value as raw bytes. The returned array might be longer than the
     * value. Access only the elements between the indexes zero, inclusive, and
     * the length, exclusive.
     *
     * @return the value as raw bytes
     * @see #length()
     */
    byte[] asBytes();

    /**
     * Get a subsequence from the value.
     *
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @throws IndexOutOfBoundsException if the start index is smaller than
     *     zero, the start index is greater than the end index, or the end
     *     index is greater than the length
     */
    CharSequence subSequence(int start, int end);

    /**
     * Get a string representation of the value.
     *
     * @return a string representation of the value
     */
    String toString();

}
