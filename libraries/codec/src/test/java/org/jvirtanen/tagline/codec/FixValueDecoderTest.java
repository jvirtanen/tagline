/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixValueDecoderTest {

    private FixValueDecoder decoder;

    @BeforeEach
    void setUp() {
        decoder = new FixValueDecoder();
    }

    @Test
    void initial() {
        assertEquals(0, decoder.length());
        assertEquals("", bytes());
    }

    @Test
    void empty() {
        assertEquals(1, decode("\u0001"));
        assertEquals(0, decoder.length());
        assertEquals("", bytes());
    }

    @Test
    void oneByte() {
        assertEquals(2, decode("a\u0001"));
        assertEquals(1, decoder.length());
        assertEquals("a", bytes());
    }

    @Test
    void twoBytes() {
        assertEquals(3, decode("ab\u0001"));
        assertEquals(2, decoder.length());
        assertEquals("ab", bytes());
    }

    @Test
    void expand() {
        var value = "a".repeat(65);

        assertEquals(66, decode(String.format("%s\u0001", value)));
        assertEquals(65, decoder.length());
        assertEquals(value, bytes());
    }

    private long decode(final String value) {
        var buffer = Unpooled.copiedBuffer(value, ISO_8859_1);

        return decoder.decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    private String bytes() {
        return new String(decoder.bytes(), 0, decoder.length(), ISO_8859_1);
    }

}
