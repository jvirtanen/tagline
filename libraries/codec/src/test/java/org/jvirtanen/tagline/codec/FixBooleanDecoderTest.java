/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FixBooleanDecoderTest {

    @Test
    void yes() {
        assertTrue(decode("Y"));
    }

    @Test
    void no() {
        assertFalse(decode("N"));
    }

    @Test
    void invalidLength() {
        assertNotBoolean("Yes");
    }

    @Test
    void invalidByte() {
        assertNotBoolean("T");
    }

    private static boolean decode(final String value) {
        var bytes = value.getBytes(ISO_8859_1);

        return FixBooleanDecoder.decode(bytes, bytes.length);
    }

    private static void assertNotBoolean(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a Boolean", exception.getMessage());
    }

}
