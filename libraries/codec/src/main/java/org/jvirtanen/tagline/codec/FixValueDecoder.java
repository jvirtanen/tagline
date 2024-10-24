/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import java.util.Arrays;

class FixValueDecoder implements ByteProcessor {

    static int decode(final byte[] bytes, int offset, final int length) {
        while (offset < length) {
            if (bytes[offset] == SOH)
                return offset + 1;

            offset++;
        }

        return -1;
    }

    private static final int INITIAL_CAPACITY = 64;

    private int capacity;

    private byte[] bytes;

    private int count;

    FixValueDecoder() {
        capacity = INITIAL_CAPACITY;

        bytes = new byte[capacity];

        count = 0;
    }

    byte[] bytes() {
        return bytes;
    }

    int length() {
        return count;
    }

    int decode(final ByteBuf buffer, final int offset, final int length) {
        count = 0;

        int sohIndex = buffer.forEachByte(offset, length, this);
        if (sohIndex < 0)
            return sohIndex;

        return sohIndex + 1;
    }

    @Override
    public boolean process(final byte value) {
        if (value == SOH)
            return false;

        if (count == capacity)
            expand();

        bytes[count++] = value;

        return true;
    }

    private void expand() {
        capacity *= 2;

        bytes = Arrays.copyOf(bytes, capacity);
    }

}
