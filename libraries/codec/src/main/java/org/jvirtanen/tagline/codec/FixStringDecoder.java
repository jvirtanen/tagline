/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

class FixStringDecoder {

    static char charAt(final byte[] bytes, final int index, final int length) {
        return (char)(bytes[index] & 0xff);
    }

    static CharSequence subSequence(final byte[] bytes, final int start, final int end) {
        return new String(bytes, start, end - start, ISO_8859_1);
    }

    static String toString(final byte[] bytes, final int offset, final int length) {
        return new String(bytes, offset, length, ISO_8859_1);
    }

}
