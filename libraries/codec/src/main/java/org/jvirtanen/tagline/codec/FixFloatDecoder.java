/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixFloatDecoder {

    private static final int MIN_NEGATIVE_EXACT_LENGTH = 20;
    private static final int MIN_POSITIVE_EXACT_LENGTH = 19;

    private static final int MAX_NEGATIVE_LENGTH = 21;
    private static final int MAX_POSITIVE_LENGTH = 20;

    static void decode(final byte[] bytes, final int index, final int length, final FixFloat container) {
        if (length == 0)
            notFloat();

        if (bytes[index] == '-') {
            if (length >= MIN_NEGATIVE_EXACT_LENGTH)
                decodeNegativeExact(bytes, index, length, container);
            else
                decodeNegative(bytes, index, length, container);
        } else {
            if (length >= MIN_POSITIVE_EXACT_LENGTH)
                decodePositiveExact(bytes, index, length, container);
            else
                decodePositive(bytes, index, length, container);
        }
    }

    private static void decodePositive(final byte[] bytes, int index, final int length, final FixFloat container) {
        int endIndex = index + length;

        byte b = bytes[index++];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = b - '0';

        while (index < endIndex) {
            b = bytes[index++];
            if (b == '.')
                break;

            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + b - '0';
        }

        if (b == '.' && index == endIndex)
            notFloat();

        int scale = 0;

        while (index < endIndex) {
            b = bytes[index++];
            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + b - '0';
            scale++;
        }

        container.setValue(unscaledValue, scale);
    }

    private static void decodePositiveExact(final byte[] bytes, int index, final int length, final FixFloat container) {
        if (length > MAX_POSITIVE_LENGTH)
            unrepresentableFloat();

        int endIndex = index + length;

        byte b = bytes[index++];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = b - '0';

        while (index < endIndex) {
            b = bytes[index++];
            if (b == '.')
                break;

            if (b < '0' || b > '9')
                notFloat();

            try {
                unscaledValue = Math.addExact(Math.multiplyExact(unscaledValue, 10), b - '0');
            } catch (ArithmeticException e) {
                unrepresentableFloat();
            }
        }

        if (b == '.' && index == endIndex)
            notFloat();

        int scale = 0;

        while (index < endIndex) {
            b = bytes[index++];
            if (b < '0' || b > '9')
                notFloat();

            try {
                unscaledValue = Math.addExact(Math.multiplyExact(unscaledValue, 10), b - '0');
            } catch (ArithmeticException e) {
                unrepresentableFloat();
            }

            scale++;
        }

        container.setValue(unscaledValue, scale);
    }

    private static void decodeNegative(final byte[] bytes, int index, final int length, final FixFloat container) {
        if (length < 2)
            notFloat();

        int endIndex = index++ + length;

        byte b = bytes[index++];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = '0' - b;

        while (index < endIndex) {
            b = bytes[index++];
            if (b == '.')
                break;

            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + '0' - b;
        }

        if (b == '.' && index == endIndex)
            notFloat();

        int scale = 0;

        while (index < endIndex) {
            b = bytes[index++];
            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + '0' - b;
            scale++;
        }

        container.setValue(unscaledValue, scale);
    }

    private static void decodeNegativeExact(final byte[] bytes, int index, final int length, final FixFloat container) {
        if (length > MAX_NEGATIVE_LENGTH)
            unrepresentableFloat();

        int endIndex = index++ + length;

        byte b = bytes[index++];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = '0' - b;

        while (index < endIndex) {
            b = bytes[index++];
            if (b == '.')
                break;

            if (b < '0' || b > '9')
                notFloat();

            try {
                unscaledValue = Math.addExact(Math.multiplyExact(unscaledValue, 10), '0' - b);
            } catch (ArithmeticException e) {
                unrepresentableFloat();
            }
        }

        if (b == '.' && index == endIndex)
            notFloat();

        int scale = 0;

        while (index < endIndex) {
            b = bytes[index++];
            if (b < '0' || b > '9')
                notFloat();

            try {
                unscaledValue = Math.addExact(Math.multiplyExact(unscaledValue, 10), '0' - b);
            } catch (ArithmeticException e) {
                unrepresentableFloat();
            }

            scale++;
        }

        container.setValue(unscaledValue, scale);
    }

    private static void notFloat() {
        throw new FixDecoderException("Not a Float");
    }

    private static void unrepresentableFloat() {
        throw new FixDecoderException("Unrepresentable Float");
    }

}
