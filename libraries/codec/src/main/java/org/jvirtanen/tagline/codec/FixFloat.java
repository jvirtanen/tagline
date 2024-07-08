/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * A FIX Float. This is a mutable container for a decimal number represented
 * in terms of a {@code long} unscaled value and an {@code int} scale. The
 * scale specifies the number of digits to the right of the decimal point and
 * must be at least zero and at most 18. The value of a Float is therefore
 * {@code unscaledValue * Math.pow(10, -scale)}.
 */
public interface FixFloat {

    /**
     * Get the unscaled value.
     *
     * @return the unscaled value
     */
    long unscaledValue();

    /**
     * Get the scale.
     *
     * @return the scale
     */
    int scale();

    /**
     * Set the value.
     *
     * @param unscaledValue the unscaled value
     * @param scale the scale
     * @return the current instance
     * @throws IllegalArgumentException if the scale is less than zero or
     *     greater than 18
     */
    FixFloat setValue(long unscaledValue, int scale);

}
