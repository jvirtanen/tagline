/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixFloatEncoderTest {

    @Test
    void zero() {
        assertEquals("0\u0001", encode(0, 0));
    }

    @Test
    void one() {
        assertEquals("1\u0001", encode(1, 0));
    }

    @Test
    void twelve() {
        assertEquals("12\u0001", encode(12, 0));
    }

    @Test
    void zeroPointZero() {
        assertEquals("0.0\u0001", encode(0, 1));
    }

    @Test
    void zeroPointOne() {
        assertEquals("0.1\u0001", encode(1, 1));
    }

    @Test
    void onePointTwo() {
        assertEquals("1.2\u0001", encode(12, 1));
    }

    @Test
    void minusOne() {
        assertEquals("-1\u0001", encode(-1, 0));
    }

    @Test
    void minusTwelve() {
        assertEquals("-12\u0001", encode(-12, 0));
    }

    @Test
    void minusZeroPointOne() {
        assertEquals("-0.1\u0001", encode(-1, 1));
    }

    @Test
    void minusOnePointTwo() {
        assertEquals("-1.2\u0001", encode(-12, 1));
    }

    @Test
    void smallestPositiveValue() {
        assertEquals("0.000000000000000001\u0001", encode(1, 18));
    }

    @Test
    void smallestNegativeValue() {
        assertEquals("-0.000000000000000001\u0001", encode(-1, 18));
    }

    @Test
    void minValue() {
        assertEquals(String.format("%s\u0001", Long.MIN_VALUE), encode(Long.MIN_VALUE, 0));
    }

    @Test
    void maxValue() {
        assertEquals(String.format("%s\u0001", Long.MAX_VALUE), encode(Long.MAX_VALUE, 0));
    }

    private static String encode(final long unscaledValue, final int scale) {
        var buffer = Unpooled.buffer();
        var value = new DefaultFixFloat(unscaledValue, scale);

        new FixFloatEncoder().encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
