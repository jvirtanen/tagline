/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixVersionDecoder {

    private FixVersion version;

    FixVersionDecoder() {
        version = null;
    }

    FixVersion version() {
        return version;
    }

    int decode(final ByteBuf buffer, final int offset, final int length) {
        if (version != null && version.matches(buffer, offset, length))
            return offset + version.length();
        else
            return setVersion(buffer, offset, length);
    }

    private int setVersion(final ByteBuf buffer, final int offset, final int length) {
        int sohIndex = buffer.indexOf(offset, offset + length, SOH);
        if (sohIndex < 0)
            return sohIndex;

        if (sohIndex == offset)
            notBeginString();

        version = FixVersion.fromBuffer(buffer, offset, sohIndex + 1 - offset);

        return sohIndex + 1;
    }

    private static void notBeginString() {
        throw new FixDecoderException("Not a BeginString(8) value");
    }

}
