/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixMediumEncoderTest {

    @Test
    void zero() {
        assertEquals("0\u0001", encode(0, SOH));
    }

    @Test
    void oneDigit() {
        assertEquals("1=", encode(1, EQUALS));
    }

    @Test
    void twoDigits() {
        assertEquals("12=", encode(12, EQUALS));
    }

    @Test
    void threeDigits() {
        assertEquals("123=", encode(123, EQUALS));
    }

    @Test
    void fourDigits() {
        assertEquals("1234=", encode(1234, EQUALS));
    }

    @Test
    void fiveDigits() {
        assertEquals("12345=", encode(12345, EQUALS));
    }

    @Test
    void sixDigits() {
        assertEquals("123456=", encode(123456, EQUALS));
    }

    @Test
    void sevenDigits() {
        assertEquals("1234567=", encode(1234567, EQUALS));
    }

    @Test
    void maxValue() {
        assertEquals("9999999=", encode(9999999, EQUALS));
    }

    @Test
    void tooSmallMedium() {
        assertNotMedium(-1, EQUALS, "Negative integer");
    }

    @Test
    void tooLargeMedium() {
        assertNotMedium(12345678, EQUALS, "Too large integer");
    }

    private static String encode(final int value, final byte separator) {
        var buffer = Unpooled.buffer();

        FixMediumEncoder.encode(value, separator, buffer);

        return buffer.toString(ISO_8859_1);
    }

    private static void assertNotMedium(final int value, final byte separator, final String message) {
        var exception = assertThrows(IllegalArgumentException.class, () -> encode(value, separator));

        assertEquals(message, exception.getMessage());
    }

}
