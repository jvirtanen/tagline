/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixBooleanEncoder {

    static void encode(final boolean value, final ByteBuf buffer) {
        buffer.writeShort(value ? YES_SHORT : NO_SHORT);
    }

}
