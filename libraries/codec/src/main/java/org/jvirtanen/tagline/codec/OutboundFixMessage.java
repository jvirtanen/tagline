/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * <p>An outbound FIX message.</p>
 *
 * <p>The {@linkplain #content() content} of an outbound FIX message contains
 * all the bytes in the outgoing message with the exception of the
 * BeginString(8), BodyLength(9), and CheckSum(10) fields, which {@link
 * OutboundFixMessageEncoder} generates on demand. When you invoke a method to
 * add a field, the method immediately encodes and appends the field into the
 * content.</p>
 */
public interface OutboundFixMessage extends FixMessage {

    /**
     * Add a Boolean field.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asBoolean()
     */
    OutboundFixMessage addBoolean(int tag, boolean value);

    /**
     * Add a Char field. If the value is not in the ISO-8859-1 character set,
     * it will be encoded as {@code '?'}.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asChar()
     */
    OutboundFixMessage addChar(int tag, char value);

    /**
     * Add an Int field.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asInt()
     */
    OutboundFixMessage addInt(int tag, long value);

    /**
     * Add a Float field.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asFloat(FixFloat)
     */
    OutboundFixMessage addFloat(int tag, FixFloat value);

    /**
     * Add a Float field. The scale specifies the number of digits to the right
     * of the decimal point in the encoded form.
     *
     * @param tag the tag
     * @param value the value
     * @param scale the scale
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999 or if the scale is less than 0 or greater than 18
     * @see FixValue#asFloat()
     */
    OutboundFixMessage addFloat(int tag, double value, int scale);

    /**
     * Add a String field. If the value contains characters that are not in the
     * ISO-8859-1 character set, they will be encoded as {@code '?'}.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asString()
     */
    OutboundFixMessage addString(int tag, CharSequence value);

    /**
     * Add a UTCDate or LocalMktDate field.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asDate(FixDate)
     */
    OutboundFixMessage addDate(int tag, FixDate value);

    /**
     * Add a UTCTimeOnly field.
     *
     * @param tag the tag
     * @param value the value
     * @param format the format
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asTime(FixTime)
     */
    OutboundFixMessage addTime(int tag, FixTime value, FixTimeFormat format);

    /**
     * Add a UTCTimestamp field.
     *
     * @param tag the tag
     * @param value the value
     * @param format the format
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asTimestamp(FixTimestamp)
     */
    OutboundFixMessage addTimestamp(int tag, FixTimestamp value, FixTimeFormat format);

    /**
     * Add a UTCTimestamp field.
     *
     * @param tag the tag
     * @param value the value as a number of milliseconds from the epoch of
     *   1970-01-01T00:00:00.000Z
     * @param format the format
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asTimestamp()
     */
    OutboundFixMessage addTimestamp(int tag, long value, FixTimeFormat format);

    /**
     * Add raw bytes.
     *
     * @param tag the tag
     * @param value the value
     * @return this instance
     * @throws IllegalArgumentException if the tag is less than 0 or greater
     *     than 9999999
     * @see FixValue#asBytes(byte[])
     */
    OutboundFixMessage addBytes(int tag, byte[] value);

}
