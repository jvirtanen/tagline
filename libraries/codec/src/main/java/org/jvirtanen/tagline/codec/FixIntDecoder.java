/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixIntDecoder {

    private static final int MIN_NEGATIVE_EXACT_LENGTH = 20;
    private static final int MIN_POSITIVE_EXACT_LENGTH = 19;

    static long decode(final byte[] bytes, final int length) {
        if (length == 0)
            notInt();

        if (bytes[0] == '-') {
            if (length >= MIN_NEGATIVE_EXACT_LENGTH)
                return decodeNegativeExact(bytes, length);
            else
                return decodeNegative(bytes, length);
        } else {
            if (length >= MIN_POSITIVE_EXACT_LENGTH)
                return decodePositiveExact(bytes, length);
            else
                return decodePositive(bytes, length);
        }
    }

    private static long decodePositive(final byte[] bytes, final int length) {
        long value = 0;

        for (int i = 0; i < length; i++) {
            byte b = bytes[i];
            if (b < '0' || b > '9')
                notInt();

            value = 10 * value + b - '0';
        }

        return value;
    }

    private static long decodePositiveExact(final byte[] bytes, final int length) {
        if (length > MIN_POSITIVE_EXACT_LENGTH)
            tooLargeInt();

        long value = 0;

        for (int i = 0; i < length; i++) {
            byte b = bytes[i];
            if (b < '0' || b > '9')
                notInt();

            try {
                value = Math.addExact(value * 10, b - '0');
            } catch (ArithmeticException e) {
                tooLargeInt();
            }
        }

        return value;
    }

    private static long decodeNegative(final byte[] bytes, final int length) {
        if (length < 2)
            notInt();

        long value = 0;

        for (int i = 1; i < length; i++) {
            byte b = bytes[i];
            if (b < '0' || b > '9')
                notInt();

            value = 10 * value + '0' - b;
        }

        return value;
    }

    private static long decodeNegativeExact(final byte[] bytes, final int length) {
        if (length > MIN_NEGATIVE_EXACT_LENGTH)
            tooSmallInt();

        long value = 0;

        for (int i = 1; i < length; i++) {
            byte b = bytes[i];
            if (b < '0' || b > '9')
                notInt();

            try {
                value = Math.addExact(value * 10, '0' - b);
            } catch (ArithmeticException e) {
                tooSmallInt();
            }
        }

        return value;
    }

    private static void notInt() {
        throw new FixDecoderException("Not an Int");
    }

    private static void tooLargeInt() {
        throw new FixDecoderException("Too large Int");
    }

    private static void tooSmallInt() {
        throw new FixDecoderException("Too small Int");
    }

}
