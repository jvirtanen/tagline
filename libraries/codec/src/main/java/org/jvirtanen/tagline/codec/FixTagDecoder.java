/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixTagDecoder {

    private int tag;

    int tag() {
        return tag;
    }

    int decode(final byte[] bytes, int index, final int length) {
        byte b = bytes[index++];
        if (b < '1' || b > '9')
            invalidTag();

        tag = b - '0';

        while (index < length) {
            b = bytes[index++];
            if (b == EQUALS)
                break;

            if (b < '0' || b > '9')
                invalidTag();

            tag = 10 * tag + b - '0';
            if (tag < 0)
                invalidTag();
        }

        if (b != EQUALS)
            return -1;

        return index;
    }

    private static void invalidTag() {
        throw new FixDecoderException("Invalid tag");
    }

}
