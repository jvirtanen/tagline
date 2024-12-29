/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FixValueDecoderTest {

    @Test
    void empty() {
        assertEquals(1, decode("\u0001"));
    }

    @Test
    void oneByte() {
        assertEquals(2, decode("a\u0001"));
    }

    @Test
    void twoBytes() {
        assertEquals(3, decode("ab\u0001"));
    }

    private long decode(final String value) {
        var bytes = value.getBytes(ISO_8859_1);

        return FixValueDecoder.decode(bytes, 0, bytes.length);
    }

}
