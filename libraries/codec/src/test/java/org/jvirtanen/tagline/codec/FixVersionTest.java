/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

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
    void bytes() {
        byte[] bytes = { 'F', 'I', 'X', '.', '4', '.', '2', SOH, };

        assertArrayEquals(bytes, FIX_4_2.bytes());
    }

    @Test
    void bits() {
        assertEquals(0x4649582e342e3201l, FIX_4_2.bits());
    }

    @Test
    void noBits() {
        assertEquals(0, FIXT_1_1.bits());
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

}
