/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ByteArrayDecoderTest {

    @Test
    void digit() {
        assertEquals(1, decodeDigit(getBytes("1"), 0));
    }

    @Test
    void twoDigits() {
        assertEquals(1, decodeTwoDigits(getBytes("20240107"), 4));
    }

    @Test
    void threeDigits() {
        assertEquals(950, decodeThreeDigits(getBytes("16:44:30.950"), 9));
    }

    @Test
    void fourDigits() {
        assertEquals(2024, decodeFourDigits(getBytes("20240107"), 0));
    }

    @Test
    void invalidByte() {
        assertNotDigit(() -> decodeDigit(getBytes("a"), 0));
    }

    private static byte[] getBytes(final String value) {
        return value.getBytes(ISO_8859_1);
    }

    private static void assertNotDigit(final Executable executable) {
        var exception = assertThrows(IllegalArgumentException.class, executable);

        assertEquals("Not a digit", exception.getMessage());
    }

}

