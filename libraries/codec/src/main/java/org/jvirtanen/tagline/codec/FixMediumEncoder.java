/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixMediumEncoder {

    static final int MAX_LENGTH = 8;

    static void encode(int value, byte separator, final ByteBuf buffer) {
        if (value < 0)
            negativeMedium();

        long bits = separator;
        int length = 1;

        do {
            bits <<= 8;
            bits |= '0' + value % 10;

            value /= 10;
            length++;
        } while (value > 0);

        bits = Long.reverseBytes(bits);

        switch (length) {
        case 2:
            buffer.writeShort((short)(bits >> 48));
            break;
        case 3:
            buffer.writeMedium((int)(bits >> 40));
            break;
        case 4:
            buffer.writeInt((int)(bits >> 32));
            break;
        case 5:
            buffer.writeInt((int)(bits >> 32));
            buffer.writeByte((int)(bits >> 24));
            break;
        case 6:
            buffer.writeInt((int)(bits >> 32));
            buffer.writeShort((int)(bits >> 16));
            break;
        case 7:
            buffer.writeInt((int)(bits >> 32));
            buffer.writeMedium((int)(bits >> 8));
            break;
        case 8:
            buffer.writeLong(bits);
            break;
        default:
            tooLargeMedium();
        }
    }

    private static void negativeMedium() {
        throw new IllegalArgumentException("Negative integer");
    }

    private static void tooLargeMedium() {
        throw new IllegalArgumentException("Too large integer");
    }

}
