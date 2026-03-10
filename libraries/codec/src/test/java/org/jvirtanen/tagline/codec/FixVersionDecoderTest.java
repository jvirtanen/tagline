/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixVersionDecoderTest {

    private FixVersionDecoder decoder;

    @BeforeEach
    void setUp() {
        decoder = new FixVersionDecoder();
    }

    @Test
    void initial() {
        assertNull(decoder.version());
    }

    @Test
    void partial() {
        assertEquals(-1, decode("FIX.4.2"));
        assertNull(decoder.version());
    }

    @Test
    void complete() {
        assertEquals(8, decode("FIX.4.2\u0001"));
        assertEquals(FIX_4_2, decoder.version());
    }

    @Test
    void equals() {
        assertEquals(8, decode("FIX.4.2\u0001"));

        var firstVersion = decoder.version();
        int firstHashCode = System.identityHashCode(firstVersion);

        assertEquals(8, decode("FIX.4.2\u0001"));

        var secondVersion = decoder.version();
        int secondHashCode = System.identityHashCode(secondVersion);

        assertEquals(firstVersion, secondVersion);
        assertEquals(firstHashCode, secondHashCode);
    }

    @Test
    void doesNotEqual() {
        assertEquals(8, decode("FIX.4.2\u0001"));

        assertEquals(9, decode("FIXT.1.1\u0001"));
        assertEquals(FIXT_1_1, decoder.version());
    }

    @Test
    void empty() {
        assertNotBeginString("\u0001");
    }

    private long decode(final String value) {
        var buffer = Unpooled.copiedBuffer(value, ISO_8859_1);

        return decoder.decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    private void assertNotBeginString(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a BeginString(8) value", exception.getMessage());
    }

}
