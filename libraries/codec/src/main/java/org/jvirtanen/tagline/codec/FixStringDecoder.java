/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

class FixStringDecoder {

    static char charAt(final int index, final byte[] bytes, final int length) {
        if (index >= length)
            indexOutOfBounds();

        return (char)(bytes[index] & 0xff);
    }

    static CharSequence subSequence(final int start, final int end, final byte[] bytes, final int length) {
        return new String(bytes, start, end - start, ISO_8859_1);
    }

    static String toString(final byte[] bytes, final int length) {
        return new String(bytes, 0, length, ISO_8859_1);
    }

    private static void indexOutOfBounds() {
        throw new IndexOutOfBoundsException();
    }

}
