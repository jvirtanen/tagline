/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

class FixTagDecoder implements ByteProcessor {

    private int lowerBound;

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

    int decode(final ByteBuf buffer, final int offset, final int length) {
        lowerBound = '1';

        tag = 0;

        int equalsIndex = buffer.forEachByte(offset, length, this);
        if (equalsIndex < 0)
            return equalsIndex;

        if (tag == 0)
            invalidTag();

        return equalsIndex + 1;
    }

    @Override
    public boolean process(final byte value) {
        if (value == EQUALS)
            return false;

        if (value < lowerBound || value > '9')
            invalidTag();

        lowerBound = '0';

        tag = 10 * tag + value - '0';
        if (tag < 0)
            invalidTag();

        return true;
    }

    private static void invalidTag() {
        throw new FixDecoderException("Invalid tag");
    }

}
