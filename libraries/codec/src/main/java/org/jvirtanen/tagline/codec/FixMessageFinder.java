/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

class FixMessageFinder implements ByteProcessor {

    private byte nextValue;

    int find(final ByteBuf buffer, int index, int length) {
        nextValue = SOH;

        int equalsIndex = buffer.forEachByte(index, length, this);
        if (equalsIndex < 0)
            return equalsIndex;

        return equalsIndex - 1;
    }

    @Override
    public boolean process(final byte value) {
        if (value != nextValue) {
            nextValue = SOH;

            return true;
        }

        switch (value) {
        case SOH:
            nextValue = '8';
            break;
        case '8':
            nextValue = EQUALS;
            break;
        case EQUALS:
            return false;
        }

        return true;
    }

}
