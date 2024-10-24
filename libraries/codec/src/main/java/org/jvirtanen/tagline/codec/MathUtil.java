/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

class MathUtil {

    private static final int POWERS_OF_TWO[] = {
        1 << 0,
        1 << 1,
        1 << 2,
        1 << 3,
        1 << 4,
        1 << 5,
        1 << 6,
        1 << 7,
        1 << 8,
        1 << 9,
        1 << 10,
        1 << 11,
        1 << 12,
        1 << 13,
        1 << 14,
        1 << 15,
        1 << 16,
        1 << 17,
        1 << 18,
        1 << 19,
        1 << 20,
        1 << 21,
        1 << 22,
        1 << 23,
        1 << 24,
        1 << 25,
        1 << 26,
        1 << 27,
        1 << 28,
        1 << 29,
        1 << 30,
    };

    static int findNextPositivePowerOfTwo(final int x) {
        for (int powerOfTwo : POWERS_OF_TWO) {
            if (x > powerOfTwo)
                continue;

            return powerOfTwo;
        }

        return Integer.MAX_VALUE;
    }

}
