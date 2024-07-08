/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

class FixCharDecoder {

    static char decode(final byte[] bytes, final int length) {
        if (length != 1)
            notChar();

        return (char)(bytes[0] & 0xff);
    }

    private static void notChar() {
        throw new FixDecoderException("Not a Char");
    }

}
