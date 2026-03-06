/*
 * Copyright 2024 Jussi Virtanen
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

/**
 * <p>A FIX value.</p>
 *
 * <p>Any FIX value can be treated as a String, hence this interface extends
 * {@link CharSequence}. You can typically pass a {@code FixValue} to most
 * methods you would pass a {@link String} as these often accept a {@code
 * CharSequence}.</p>
 *
 * <p>This interface also contains methods to decode this value as other data
 * types, such as Boolean, Char, or Int. Note that this decoding happens on
 * demand whenever you invoke these methods. For the best performance, you
 * should store their return values instead of invoking them multiple
 * times.</p>
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
     * @see OutboundFixMessage#addBoolean(int,boolean)
     */
    boolean asBoolean();

    /**
     * Get this value as a Char.
     *
     * @return this value as a Char
     * @throws FixDecoderException if this value is not a Char
     * @see OutboundFixMessage#addChar(int,char)
     */
    char asChar();

    /**
     * Get this value as an Int.
     *
     * @return this value as an Int
     * @throws FixDecoderException if this value is not an Int or cannot be
     *     represented as a {@code long}
     * @see OutboundFixMessage#addInt(int,long)
     */
    long asInt();

    /**
     * Get this value as a Float.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a Float or cannot be
     *     represented as a {@link FixFloat}
     * @see OutboundFixMessage#addFloat(int,FixFloat)
     */
    void asFloat(FixFloat container);

    /**
     * Get this value as a Float.
     *
     * @return this value as a Float
     * @throws FixDecoderException if this value is not a Float or cannot be
     *     represented as a {@link FixFloat}
     * @see OutboundFixMessage#addFloat(int,double,int)
     */
    double asFloat();

    /**
     * Get this value as a String.
     *
     * @return this value as a String
     * @see OutboundFixMessage#addString(int,CharSequence)
     */
    CharSequence asString();

    /**
     * Get this value as a UTCDate or LocalMktDate.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a UTCDate or
     *     LocalMktDate
     * @see OutboundFixMessage#addDate(int,FixDate)
     */
    void asDate(FixDate container);

    /**
     * Get this value as a UTCTimeOnly.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a UTCTimeOnly
     * @see OutboundFixMessage#addTime(int,FixTime,FixTimeFormat)
     */
    void asTime(FixTime container);

    /**
     * Get this value as a UTCTimestamp.
     *
     * @param container a value container
     * @throws FixDecoderException if this value is not a UTCTimestamp
     * @see OutboundFixMessage#addTimestamp(int,FixTimestamp,FixTimeFormat)
     */
    void asTimestamp(FixTimestamp container);

    /**
     * Get this value as a UTCTimestamp.
     *
     * @return this value as number of milliseconds from the epoch of
     *     1970-01-01T00:00:00.000Z
     * @throws FixDecoderException if this value is not a UTCTimestamp
     * @see OutboundFixMessage#addTimestamp(int,long,FixTimeFormat)
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
     * @see OutboundFixMessage#addBytes(int,byte[])
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
