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

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixTimestampDecoderTest {

    private FixTimestamp timestamp;

    @BeforeEach
    void setUp() {
        timestamp = new DefaultFixTimestamp();
    }

    @Test
    void withSeconds() {
        decode("20240107-16:44:30");

        assertEquals(2024, timestamp.year());
        assertEquals(1, timestamp.month());
        assertEquals(7, timestamp.day());
        assertEquals(16, timestamp.hour());
        assertEquals(44, timestamp.minute());
        assertEquals(30, timestamp.second());
        assertEquals(0, timestamp.milli());
    }

    @Test
    void withMilliseconds() {
        decode("20240107-16:44:30.950");

        assertEquals(2024, timestamp.year());
        assertEquals(1, timestamp.month());
        assertEquals(7, timestamp.day());
        assertEquals(16, timestamp.hour());
        assertEquals(44, timestamp.minute());
        assertEquals(30, timestamp.second());
        assertEquals(950, timestamp.milli());
    }

    @Test
    void invalidLength() {
        notTimestamp("20240107");
    }

    @Test
    void invalidByteAtMinus() {
        notTimestamp("20240107+16:44:30");
    }

    @Test
    void invalidByteAtFirstColon() {
        notTimestamp("20240107-16;44:30");
    }

    @Test
    void invalidByteAtSecondColon() {
        notTimestamp("20240107-16:44;30");
    }

    @Test
    void invalidByteAtPeriod() {
        notTimestamp("20240107-16:44:30,950");
    }

    @Test
    void invalidComponent() {
        notTimestamp("20240132-16:44:30");
    }

    private void decode(final String value) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);

        FixTimestampDecoder.decode(bytes, 1, bytes.length - 2, timestamp);
    }

    private void notTimestamp(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a UTCTimestamp", exception.getMessage());
    }

}
