/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;

class FixVersionEncoder {

    static void encode(final FixVersion version, final ByteBuf buffer) {
        long bits = version.bits();

        if (bits != 0)
            buffer.writeLong(bits);
        else
            buffer.writeBytes(version.bytes());
    }

}
