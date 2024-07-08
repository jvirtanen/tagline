/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

class FixTagDecoder implements ByteProcessor {

    private int tag;

    int tag() {
        return tag;
    }

    int decode(final ByteBuf buffer, final int offset, final int length) {
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

        int lowerBound = tag == 0 ? '1' : '0';

        if (value < lowerBound || value > '9')
            invalidTag();

        tag = 10 * tag + value - '0';
        if (tag < 0)
            invalidTag();

        return true;
    }

    private static void invalidTag() {
        throw new FixDecoderException("Invalid tag");
    }

}
