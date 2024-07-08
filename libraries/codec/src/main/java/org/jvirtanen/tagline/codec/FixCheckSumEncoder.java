/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixCheckSumEncoder {

    static void encode(final int checkSum, final ByteBuf buffer) {
        buffer.writeInt(('0' + checkSum / 100 % 10) << 24
                | ('0' + checkSum / 10 % 10) << 16
                | ('0' + checkSum % 10) << 8
                | SOH);
    }

}
