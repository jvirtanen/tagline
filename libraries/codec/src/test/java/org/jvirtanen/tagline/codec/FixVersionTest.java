/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixVersionTest {

    @Test
    void beginString() {
        assertEquals("FIX.4.2", FIX_4_2.beginString());
    }

    @Test
    void asString() {
        assertEquals("FIX.4.2", FIX_4_2.toString());
    }

    @Test
    void ofBytes() {
        var buffer = copiedBuffer("\u0000\u0000FIX.4.2\u0001\u0000\u0000");

        assertEquals(FIX_4_2, FixVersion.of(buffer, 2, 8));
    }

    @Test
    void equals() {
        assertEquals(FIX_4_2, FixVersion.of("FIX.4.2"));
    }

    @Test
    void doesNotEqual() {
        assertNotEquals(FIX_4_2, FIX_4_3);
    }

    @Test
    void length() {
        assertEquals(8, FIX_4_2.length());
    }

    @Test
    void encodeBits() {
        assertEquals("FIX.4.2\u0001", encode(FIX_4_2));
    }

    @Test
    void encodeBytes() {
        assertEquals("FIXT.1.1\u0001", encode(FIXT_1_1));
    }

    @Test
    void bitsMatch() {
        var buffer = copiedBuffer("\u0000\u0000FIX.4.2\u0001\u0000\u0000");

        assertTrue(FIX_4_2.matches(buffer, 2, 10));
    }

    @Test
    void bitsDoNotMatch() {
        var buffer = copiedBuffer("\u0000\u0000FIX.4.3\u0001\u0000\u0000");

        assertFalse(FIX_4_2.matches(buffer, 2, 10));
    }

    @Test
    void bytesMatch() {
        var buffer = copiedBuffer("\u0000\u0000FIXT.1.1\u0001\u0000\u0000");

        assertTrue(FIXT_1_1.matches(buffer, 2, 11));
    }

    @Test
    void bytesDoNotMatch() {
        var buffer = copiedBuffer("\u0000\u0000FIX.4.3\u0001\u0000\u0000");

        assertFalse(FIXT_1_1.matches(buffer, 2, 10));
    }

    @Test
    void doesNotMatchTooFewBytes() {
        var buffer = copiedBuffer("\u0000\u0000FIX.4.");

        assertFalse(FIX_4_2.matches(buffer, 2, 6));
    }

    @Test
    void doesNotMatchTooManyBytes() {
        var buffer = copiedBuffer("\u0000\u0000FIX.4.2  \u0001");

        assertFalse(FIX_4_2.matches(buffer, 2, 10));
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

    private static String encode(final FixVersion value) {
        var buffer = Unpooled.buffer();

        value.encode(buffer);

        return buffer.toString(ISO_8859_1);
    }

}
