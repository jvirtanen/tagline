/*
 * Copyright 2024 Jussi Virtanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
