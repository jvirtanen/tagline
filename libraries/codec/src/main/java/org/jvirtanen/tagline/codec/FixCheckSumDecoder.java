/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixCheckSumDecoder {

    static int decode(final ByteBuf buffer, final int index) {
        int bits = buffer.getInt(index);

        int a = ((bits & 0xff000000) >> 24) - '0';
        int b = ((bits & 0xff0000) >> 16) - '0';
        int c = ((bits & 0xff00) >> 8) - '0';
        int d = bits & 0xff;

        if (a > 9 || b > 9 || c > 9 || d != SOH)
            notCheckSum();

        return 100 * a + 10 * b + c;
    }

    private static void notCheckSum() {
        throw new FixDecoderException("Invalid CheckSum(10) value");
    }

}
