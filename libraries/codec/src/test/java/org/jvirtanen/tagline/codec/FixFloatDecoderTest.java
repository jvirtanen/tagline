/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class FixFloatDecoderTest {

    @Test
    void zero() {
        assertEquals(of(0, 0), decode("0"));
    }

    @Test
    void one() {
        assertEquals(of(1, 0), decode("1"));
    }

    @Test
    void onePointZero() {
        assertEquals(of(10, 1), decode("1.0"));
    }

    @Test
    void twelve() {
        assertEquals(of(12, 0), decode("12"));
    }

    @Test
    void twelvePointThree() {
        assertEquals(of(123, 1), decode("12.3"));
    }

    @Test
    void minusZero() {
        assertEquals(of(0, 0), decode("-0"));
    }

    @Test
    void minusOne() {
        assertEquals(of(-1, 0), decode("-1"));
    }

    @Test
    void minusOnePointZero() {
        assertEquals(of(-10, 1), decode("-1.0"));
    }

    @Test
    void minusTwelve() {
        assertEquals(of(-12, 0), decode("-12"));
    }

    @Test
    void minusTwelvePointThree() {
        assertEquals(of(-123, 1), decode("-12.3"));
    }

    @Test
    void minValue() {
        assertEquals(of(Long.MIN_VALUE, 0), decode(Long.toString(Long.MIN_VALUE)));
    }

    @Test
    void maxValue() {
        assertEquals(of(Long.MAX_VALUE, 0), decode(Long.toString(Long.MAX_VALUE)));
    }

    @Test
    void minUnscaledValue() {
        var value = BigDecimal.valueOf(Long.MIN_VALUE).movePointLeft(18);

        assertEquals(of(Long.MIN_VALUE, 18), decode(value.toString()));
    }

    @Test
    void maxUnscaledValue() {
        var value = BigDecimal.valueOf(Long.MAX_VALUE).movePointLeft(18);

        assertEquals(of(Long.MAX_VALUE, 18), decode(value.toString()));
    }

    @Test
    void positiveLeadingZeroes() {
        assertEquals(of(123, 0), decode("00123"));
    }

    @Test
    void negativeLeadingZeroes() {
        assertEquals(of(-123, 0), decode("-00123"));
    }

    @Test
    void tooSmallValue() {
        var tooSmallValue = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

        assertNotFloat("Unrepresentable Float", tooSmallValue.toString());
    }

    @Test
    void tooLargeValue() {
        var tooLargeValue = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        assertNotFloat("Unrepresentable Float", tooLargeValue.toString());
    }

    @Test
    void tooSmallUnscaledValue() {
        var value = BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.ONE).movePointLeft(18);

        assertNotFloat("Unrepresentable Float", value.toString());
    }

    @Test
    void tooLargeUnscaledValue() {
        var value = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.ONE).movePointLeft(18);

        assertNotFloat("Unrepresentable Float", value.toString());
    }

    @Test
    void empty() {
        assertNotFloat("Not a Float", "");
    }

    @Test
    void minus() {
        assertNotFloat("Not a Float", "-");
    }

    @Test
    void point() {
        assertNotFloat("Not a Float", ".");
    }

    @Test
    void precedingPoint() {
        assertNotFloat("Not a Float", ".1");
    }

    @Test
    void trailingPoint() {
        assertNotFloat("Not a Float", "1.");
    }

    @Test
    void minusPoint() {
        assertNotFloat("Not a Float", "-.");
    }

    @Test
    void minusPrecedingPoint() {
        assertNotFloat("Not a Float", "-.1");
    }

    @Test
    void minusTrailingPoint() {
        assertNotFloat("Not a Float", "-1.");
    }

    @Test
    void invalidByte() {
        assertNotFloat("Not a Float", "Y");
    }

    private static FixFloat of(final long unscaledValue, final int scale) {
        return new DefaultFixFloat(unscaledValue, scale);
    }

    private static FixFloat decode(final String value) {
        var bytes = value.getBytes(ISO_8859_1);
        var container = new DefaultFixFloat();

        FixFloatDecoder.decode(bytes, bytes.length, container);

        return container;
    }

    private static void assertNotFloat(final String message, final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals(message, exception.getMessage());
    }

}
