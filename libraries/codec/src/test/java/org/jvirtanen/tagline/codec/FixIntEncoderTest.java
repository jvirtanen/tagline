/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixIntEncoderTest {

    @Test
    void zero() {
        assertEquals("0\u0001", encode(0));
    }

    @Test
    void one() {
        assertEquals("1\u0001", encode(1));
    }

    @Test
    void twelve() {
        assertEquals("12\u0001", encode(12));
    }

    @Test
    void minusOne() {
        assertEquals("-1\u0001", encode(-1));
    }

    @Test
    void minusTwelve() {
        assertEquals("-12\u0001", encode(-12));
    }

    @Test
    void minValue() {
        assertEquals(String.format("%s\u0001", Long.MIN_VALUE), encode(Long.MIN_VALUE));
    }

    @Test
    void maxValue() {
        assertEquals(String.format("%s\u0001", Long.MAX_VALUE), encode(Long.MAX_VALUE));
    }

    private static String encode(final long value) {
        var buffer = Unpooled.buffer();

        new FixIntEncoder().encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
