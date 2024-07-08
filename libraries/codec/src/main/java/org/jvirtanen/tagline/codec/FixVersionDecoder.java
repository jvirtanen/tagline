/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import java.util.Arrays;

class FixVersionDecoder implements ByteProcessor {

    private static final int INITIAL_CAPACITY = 16;

    private FixVersion version;

    private int capacity;

    private byte[] bytes;

    private int count;

    FixVersionDecoder() {
        version = null;

        capacity = INITIAL_CAPACITY;

        bytes = new byte[capacity];

        count = 0;
    }

    FixVersion version() {
        return version;
    }

    int decode(final ByteBuf buffer, final int offset, final int length) {
        count = 0;

        int sohIndex = buffer.forEachByte(offset, length, this);
        if (sohIndex < 0)
            return sohIndex;

        if (count == 1)
            notBeginString();

        if (version == null || !version.equals(bytes, count))
            version = FixVersion.fromBytes(bytes, count);

        return sohIndex;
    }

    @Override
    public boolean process(final byte value) {
        if (count == capacity)
            expand();

        bytes[count++] = value;

        return value != SOH;
    }

    private void expand() {
        capacity *= 2;

        bytes = Arrays.copyOf(bytes, capacity);
    }

    private static void notBeginString() {
        throw new FixDecoderException("Not a BeginString(8) value");
    }

}
