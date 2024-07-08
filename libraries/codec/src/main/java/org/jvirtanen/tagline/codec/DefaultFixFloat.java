/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import java.math.BigDecimal;

/**
 * The default implementation of a FIX Float.
 */
public class DefaultFixFloat implements FixFloat {

    private static final double[] POWERS_OF_TEN = {
        1.0,
        10.0,
        100.0,
        1000.0,
        10000.0,
        100000.0,
        1000000.0,
        10000000.0,
        100000000.0,
        1000000000.0,
        10000000000.0,
        100000000000.0,
        1000000000000.0,
        10000000000000.0,
        100000000000000.0,
        1000000000000000.0,
        10000000000000000.0,
        100000000000000000.0,
        1000000000000000000.0,
    };

    private long unscaledValue;
    private int scale;

    /**
     * Construct a new instance.
     */
    public DefaultFixFloat() {
        this(0, 0);
    }

    /**
     * Construct a new instance.
     *
     * @param unscaledValue the unscaled value
     * @param scale the scale
     * @throws IllegalArgumentException if the scale is less than zero or
     *     greater than 18
     */
    public DefaultFixFloat(final long unscaledValue, final int scale) {
        setValue(unscaledValue, scale);
    }

    /**
     * Construct a new instance.
     *
     * @param value a {@code double} value
     * @param scale the scale
     * @throws IllegalArgumentException if the scale is less than zero or
     *     greater than 18
     */
    public DefaultFixFloat(final double value, final int scale) {
        setValue(value, scale);
    }

    /**
     * Returns true if the specified object is an instance of this class and
     * its unscaled value and scale are both equal to those of this instance,
     * otherwise returns false.
     *
     * @return true if the specified object is equal to this instance,
     *     otherwise false
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof DefaultFixFloat) {
            var f = (DefaultFixFloat)obj;

            return unscaledValue == f.unscaledValue && scale == f.scale;
        }

        return false;
    }

    @Override
    public long unscaledValue() {
        return unscaledValue;
    }

    @Override
    public int scale() {
        return scale;
    }

    @Override
    public DefaultFixFloat setValue(final long unscaledValue, final int scale) {
        checkScale(scale);

        this.unscaledValue = unscaledValue;
        this.scale = scale;

        return this;
    }

    /**
     * Set the value.
     *
     * @param value a {@code double} value
     * @param scale the scale
     * @throws IllegalArgumentException if the scale is less than zero or
     *     greater than 18
     */
    public DefaultFixFloat setValue(final double value, final int scale) {
        checkScale(scale);

        this.unscaledValue = Math.round(value * POWERS_OF_TEN[scale]);
        this.scale = scale;

        return this;
    }

    /**
     * Convert this instance into a {@code double}.
     *
     * @return this instance as a {@code double}
     */
    public double doubleValue() {
        return unscaledValue / POWERS_OF_TEN[scale];
    }

    /**
     * Get a string representation of this instance.
     *
     * @return a string representation of this instance
     */
    @Override
    public String toString() {
        return BigDecimal.valueOf(unscaledValue, scale).toString();
    }

    private static void checkScale(final int scale) {
        if (scale < 0 || scale > 18)
            invalidScale();
    }

    private static void invalidScale() {
        throw new IllegalArgumentException("Invalid scale");
    }

}
