/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DefaultFixFloatTest {

    @Test
    void minScale() {
        assertEquals(0, of(0, 0).scale());
    }

    @Test
    void maxScale() {
        assertEquals(18, of(0, 18).scale());
    }

    @Test
    void tooSmallScale() {
        assertInvalidScale(-1);
    }

    @Test
    void tooLargeScale() {
        assertInvalidScale(19);
    }

    @Test
   void setZeroWithDouble() {
        assertEquals(of(0, 0), of(0.0, 0));
    }

    @Test
    void setOneWithDouble() {
        assertEquals(of(1, 0), of(1.0, 0));
    }

    @Test
    void setOnePointTwentyThreeWithDouble() {
        assertEquals(of(123, 2), of(1.23, 2));
    }

    @Test
    void setTwelvePointThreeWithDouble() {
        assertEquals(of(123, 1), of(12.3, 1));
    }

    @Test
    void zeroAsDoubleValue() {
        assertEquals(0.0, of(0, 0).doubleValue());
    }

    @Test
    void oneAsDoubleValue() {
        assertEquals(1.0, of(1, 0).doubleValue());
    }

    @Test
    void onePointTwoAsDoubleValue() {
        assertEquals(1.2, of(12, 1).doubleValue());
    }

    @Test
    void minusOneAsDoubleValue() {
        assertEquals(-1.0, of(-1, 0).doubleValue());
    }

    @Test
    void minusOnePointTwoAsDoubleValue() {
        assertEquals(-1.2, of(-12, 1).doubleValue());
    }

    @Test
    void smallestPositiveValueAsDoubleValue() {
        assertEquals(1e-18, of(1, 18).doubleValue());
    }

    @Test
    void smallestNegativeValueAsDoubleValue() {
        assertEquals(-1e-18, of(-1, 18).doubleValue());
    }

    @Test
    void minValueAsDoubleValue() {
        assertEquals((double)Long.MIN_VALUE, of(Long.MIN_VALUE, 0).doubleValue());
    }

    @Test
    void maxValueAsDoubleValue() {
        assertEquals((double)Long.MAX_VALUE, of(Long.MAX_VALUE, 0).doubleValue());
    }

    @Test
    void smallestPositiveValueAsString() {
        assertEquals("0.000000000000000001", of(1, 18).toString());
    }

    @Test
    void smallestNegativeValueAsString() {
        assertEquals("-0.000000000000000001", of(-1, 18).toString());
    }

    private static DefaultFixFloat of(final long unscaledValue, final int scale) {
        return new DefaultFixFloat(unscaledValue, scale);
    }

    private static DefaultFixFloat of(final double value, final int scale) {
        return new DefaultFixFloat(value, scale);
    }

    private static void assertInvalidScale(final int scale) {
        var exception = assertThrows(IllegalArgumentException.class, () -> of(0, scale));

        assertEquals("Invalid scale", exception.getMessage());
    }

}
