/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;

class FixDateEncoder {

    static void encode(final FixDate date, final ByteBuf buffer) {
        long year = date.year();
        long month = date.month();
        long day = date.day();

        buffer.writeLong(('0' + year / 1000) << 56
                | ('0' + year / 100 % 10) << 48
                | ('0' + year / 10 % 10) << 40
                | ('0' + year % 10) << 32
                | ('0' + month / 10) << 24
                | ('0' + month % 10) << 16
                | ('0' + day / 10) << 8
                | ('0' + day % 10));
    }

}
