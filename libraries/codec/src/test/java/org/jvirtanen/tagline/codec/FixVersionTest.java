/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

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
    void fromBytes() {
        byte[] bytes = { 'F', 'I', 'X', '.', '4', '.', '2', SOH, 0, 0, };

        assertEquals(FIX_4_2, FixVersion.fromBytes(bytes, 8));
    }

    @Test
    void equalsObject() {
        assertEquals(FIX_4_2, new FixVersion("FIX.4.2"));
    }

    @Test
    void doesNotEqualObject() {
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
    void equalsBytes() {
        byte[] bytes = { 'F', 'I', 'X', '.', '4', '.', '2', SOH, 0, 0, };

        assertTrue(FIX_4_2.equals(bytes, 8));
    }

    @Test
    void doesNotEqualBytes() {
        byte[] bytes = { 'F', 'I', 'X', '.', '4', '.', '3', SOH, 0, 0, };

        assertFalse(FIX_4_2.equals(bytes, 8));
    }

    @Test
    void doesNotEqualTooFewBytes() {
        byte[] bytes = { 'F', 'I', 'X', '.', '4', '.', };

        assertFalse(FIX_4_2.equals(bytes, 6));
    }

    @Test
    void doesNotEqualTooManyBytes() {
        byte[] bytes = { 'F', 'I', 'X', '.', '4', '.', '2', ' ', ' ', SOH, };

        assertFalse(FIX_4_2.equals(bytes, 10));
    }

    private static String encode(final FixVersion value) {
        var buffer = Unpooled.buffer();

        value.encode(buffer);

        return buffer.toString(ISO_8859_1);
    }

}
