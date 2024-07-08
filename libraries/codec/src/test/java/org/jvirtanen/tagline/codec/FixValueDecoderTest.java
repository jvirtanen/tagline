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
        assertEquals(21, decode("aaaaaaaaaaaaaaaaaaaa\u0001"));
        assertEquals(20, decoder.length());
        assertEquals("aaaaaaaaaaaaaaaaaaaa", bytes());
    }

    private long decode(final String value) {
        var buffer = Unpooled.copiedBuffer(value, ISO_8859_1);

        return decoder.decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    private String bytes() {
        return new String(decoder.bytes(), 0, decoder.length(), ISO_8859_1);
    }

}
