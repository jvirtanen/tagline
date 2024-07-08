/*
 * Copyright 2024 Jussi Virtanen
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
        var bytes = value.getBytes(ISO_8859_1);

        FixTimestampDecoder.decode(bytes, bytes.length, timestamp);
    }

    private void notTimestamp(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a UTCTimestamp", exception.getMessage());
    }

}
