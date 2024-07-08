/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class FixIntDecoderTest {

    @Test
    void zero() {
        assertEquals(0, decode("0"));
    }

    @Test
    void one() {
        assertEquals(1, decode("1"));
    }

    @Test
    void twelve() {
        assertEquals(12, decode("12"));
    }

    @Test
    void minusZero() {
        assertEquals(0, decode("-0"));
    }

    @Test
    void minusOne() {
        assertEquals(-1, decode("-1"));
    }

    @Test
    void minusTwelve() {
        assertEquals(-12, decode("-12"));
    }

    @Test
    void minValue() {
        assertEquals(Long.MIN_VALUE, decode(Long.toString(Long.MIN_VALUE)));
    }

    @Test
    void maxValue() {
        assertEquals(Long.MAX_VALUE, decode(Long.toString(Long.MAX_VALUE)));
    }

    @Test
    void positiveLeadingZeroes() {
        assertEquals(123, decode("00123"));
    }

    @Test
    void negativeLeadingZeroes() {
        assertEquals(-123, decode("-00123"));
    }

    @Test
    void tooSmallValue() {
        var tooSmallValue = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

        assertNotInt(tooSmallValue.toString(), "Too small Int");
    }

    @Test
    void tooLargeValue() {
        var tooLargeValue = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        assertNotInt(tooLargeValue.toString(), "Too large Int");
    }

    @Test
    void empty() {
        assertNotInt("", "Not an Int");
    }

    @Test
    void minus() {
        assertNotInt("-", "Not an Int");
    }

    @Test
    void invalidByte() {
        assertNotInt("Y", "Not an Int");
    }

    private static long decode(final String value) {
        var bytes = value.getBytes(ISO_8859_1);

        return FixIntDecoder.decode(bytes, bytes.length);
    }

    private static void assertNotInt(final String value, final String message) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals(message, exception.getMessage());
    }

}
