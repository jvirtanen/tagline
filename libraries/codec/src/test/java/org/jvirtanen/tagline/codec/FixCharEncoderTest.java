/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixCharEncoderTest {

    @Test
    void ascii() {
        assertEquals("A\u0001", encode('A'));
    }

    @Test
    void iso88591() {
        assertEquals("£\u0001", encode('£'));
    }

    @Test
    void unicode() {
        assertEquals("?\u0001", encode('₹'));
    }

    private static String encode(final char value) {
        var buffer = Unpooled.buffer();

        FixCharEncoder.encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
