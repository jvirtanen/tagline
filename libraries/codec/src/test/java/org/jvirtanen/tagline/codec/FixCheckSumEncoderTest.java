/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixCheckSumEncoderTest {

    @Test
    void oneDigit() {
        assertEquals("001\u0001", encode(1));
    }

    @Test
    void twoDigits() {
        assertEquals("012\u0001", encode(12));
    }

    @Test
    void threeDigits() {
        assertEquals("123\u0001", encode(123));
    }

    @Test
    void fourDigits() {
        assertEquals("230\u0001", encode(1230));
    }

    @Test
    void fiveDigits() {
        assertEquals("300\u0001", encode(12300));
    }

    private static String encode(final int value) {
        var buffer = Unpooled.buffer();

        FixCheckSumEncoder.encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
