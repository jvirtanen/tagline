/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FixCharDecoderTest {

    @Test
    void ascii() {
        assertEquals('A', decode("A"));
    }

    @Test
    void iso88591() {
        assertEquals('£', decode("£"));
    }

    @Test
    void invalidLength() {
        assertNotChar("AA");
    }

    private static char decode(final String value) {
        var bytes = value.getBytes(ISO_8859_1);

        return FixCharDecoder.decode(bytes, bytes.length);
    }

    private static void assertNotChar(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a Char", exception.getMessage());
    }

}
