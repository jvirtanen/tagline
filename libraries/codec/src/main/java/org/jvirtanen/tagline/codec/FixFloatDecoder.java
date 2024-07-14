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

    static void decode(final byte[] bytes, final int length, final FixFloat container) {
        if (length == 0)
            notFloat();

        if (bytes[0] == '-') {
            if (length >= MIN_NEGATIVE_EXACT_LENGTH)
                decodeNegativeExact(bytes, length, container);
            else
                decodeNegative(bytes, length, container);
        } else {
            if (length >= MIN_POSITIVE_EXACT_LENGTH)
                decodePositiveExact(bytes, length, container);
            else
                decodePositive(bytes, length, container);
        }
    }

    private static void decodePositive(final byte[] bytes, final int length, final FixFloat container) {
        byte b = bytes[0];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = b - '0';

        int i = 1;

        while (i < length) {
            b = bytes[i++];
            if (b == '.')
                break;

            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + b - '0';
        }

        if (b == '.' && i == length)
            notFloat();

        int scale = 0;

        while (i < length) {
            b = bytes[i++];
            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + b - '0';
            scale++;
        }

        container.setValue(unscaledValue, scale);
    }

    private static void decodePositiveExact(final byte[] bytes, final int length, final FixFloat container) {
        if (length > MAX_POSITIVE_LENGTH)
            unrepresentableFloat();

        byte b = bytes[0];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = b - '0';

        int i = 1;

        while (i < length) {
            b = bytes[i++];
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

        if (b == '.' && i == length)
            notFloat();

        int scale = 0;

        while (i < length) {
            b = bytes[i++];
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

    private static void decodeNegative(final byte[] bytes, final int length, final FixFloat container) {
        if (length < 2)
            notFloat();

        byte b = bytes[1];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = '0' - b;

        int i = 2;

        while (i < length) {
            b = bytes[i++];
            if (b == '.')
                break;

            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + '0' - b;
        }

        if (b == '.' && i == length)
            notFloat();

        int scale = 0;

        while (i < length) {
            b = bytes[i++];
            if (b < '0' || b > '9')
                notFloat();

            unscaledValue = 10 * unscaledValue + '0' - b;
            scale++;
        }

        container.setValue(unscaledValue, scale);
    }

    private static void decodeNegativeExact(final byte[] bytes, final int length, final FixFloat container) {
        if (length > MAX_NEGATIVE_LENGTH)
            unrepresentableFloat();

        byte b = bytes[1];
        if (b < '0' || b > '9')
            notFloat();

        long unscaledValue = '0' - b;

        int i = 2;

        while (i < length) {
            b = bytes[i++];
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

        if (b == '.' && i == length)
            notFloat();

        int scale = 0;

        while (i < length) {
            b = bytes[i++];
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
