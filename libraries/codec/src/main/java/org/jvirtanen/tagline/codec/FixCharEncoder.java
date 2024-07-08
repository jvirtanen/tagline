/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixCharEncoder {

    static void encode(char value, final ByteBuf buffer) {
        buffer.writeShort((value > 0xff ? '?' : value) << 8 | SOH);
    }

}
