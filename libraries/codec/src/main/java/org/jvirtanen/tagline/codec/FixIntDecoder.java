/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixIntDecoder {

    private static final int MAX_NEGATIVE_LENGTH = 20;
    private static final int MAX_POSITIVE_LENGTH = 19;

    static long decode(final byte[] bytes, final int index, final int length) {
        if (length == 0)
            notInt();

        if (bytes[index] == '-') {
            if (length >= MAX_NEGATIVE_LENGTH)
                return decodeNegativeExact(bytes, index, length);
            else
                return decodeNegative(bytes, index, length);
        } else {
            if (length >= MAX_POSITIVE_LENGTH)
                return decodePositiveExact(bytes, index, length);
            else
                return decodePositive(bytes, index, length);
        }
    }

    private static long decodePositive(final byte[] bytes, int index, final int length) {
        int endIndex = index + length;

        long value = 0;

        while (index < endIndex) {
            byte b = bytes[index++];
            if (b < '0' || b > '9')
                notInt();

            value = 10 * value + b - '0';
        }

        return value;
    }

    private static long decodePositiveExact(final byte[] bytes, int index, final int length) {
        if (length > MAX_POSITIVE_LENGTH)
            tooLargeInt();

        int endIndex = index + length;

        long value = 0;

        while (index < endIndex) {
            byte b = bytes[index++];
            if (b < '0' || b > '9')
                notInt();

            try {
                value = Math.addExact(Math.multiplyExact(value, 10), b - '0');
            } catch (ArithmeticException e) {
                tooLargeInt();
            }
        }

        return value;
    }

    private static long decodeNegative(final byte[] bytes, int index, final int length) {
        if (length < 2)
            notInt();

        int endIndex = index++ + length;

        long value = 0;

        while (index < endIndex) {
            byte b = bytes[index++];
            if (b < '0' || b > '9')
                notInt();

            value = 10 * value + '0' - b;
        }

        return value;
    }

    private static long decodeNegativeExact(final byte[] bytes, int index, final int length) {
        if (length > MAX_NEGATIVE_LENGTH)
            tooSmallInt();

        int endIndex = index++ + length;

        long value = 0;

        while (index < endIndex) {
            byte b = bytes[index++];
            if (b < '0' || b > '9')
                notInt();

            try {
                value = Math.addExact(Math.multiplyExact(value, 10), '0' - b);
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
