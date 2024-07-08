/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

class ByteArrayDecoder {

    static int decodeDigit(final byte[] bytes, final int index) {
        int b = bytes[index];
        if (b < '0' || b > '9')
            notDigit();

        return b - '0';
    }

    static int decodeTwoDigits(final byte[] bytes, final int index) {
        return 10 * decodeDigit(bytes, index) + decodeDigit(bytes, index + 1);
    }

    static int decodeThreeDigits(final byte[] bytes, final int index) {
        return 100 * decodeDigit(bytes, index) + 10 * decodeDigit(bytes, index + 1)
            + decodeDigit(bytes, index + 2);
    }

    static int decodeFourDigits(final byte[] bytes, final int index) {
        return 1000 * decodeDigit(bytes, index) + 100 * decodeDigit(bytes, index + 1)
            + 10 * decodeDigit(bytes, index + 2) + decodeDigit(bytes, index + 3);
    }

    private static void notDigit() {
        throw new IllegalArgumentException("Not a digit");
    }

}
