/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixIntEncoder {

    static final int MAX_LENGTH = 21;

    private final byte[] bytes;

    FixIntEncoder() {
        bytes = new byte[MAX_LENGTH];

        bytes[MAX_LENGTH - 1] = SOH;
    }

    void encode(final long value, final ByteBuf buffer) {
        if (value < 0)
            encodeNegative(value, buffer);
        else
            encodePositive(value, buffer);
    }

    private void encodePositive(long value, final ByteBuf buffer) {
        int i = MAX_LENGTH - 1;

        do {
             bytes[--i] = (byte)('0' + value % 10);

             value /= 10;
        } while (value > 0);

        buffer.writeBytes(bytes, i, MAX_LENGTH - i);
    }

    private void encodeNegative(long value, final ByteBuf buffer) {
        int i = MAX_LENGTH - 1;

        do {
            bytes[--i] = (byte)('0' - value % 10);

            value /= 10;
        } while (value < 0);

        bytes[--i] = '-';

        buffer.writeBytes(bytes, i, MAX_LENGTH - i);
    }

}
