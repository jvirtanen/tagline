/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixTimeDecoder {

    static void decode(final byte[] bytes, final int length, final FixTime container) {
        if (length != 8 && length != 12)
            notTime();

        try {
            container.setHour(decodeTwoDigits(bytes, 0));

            if (bytes[2] != ':')
                notTime();

            container.setMinute(decodeTwoDigits(bytes, 3));

            if (bytes[5] != ':')
                notTime();

            container.setSecond(decodeTwoDigits(bytes, 6));

            if (length == 12) {
                if (bytes[8] != '.')
                    notTime();

                container.setMilli(decodeThreeDigits(bytes, 9));
            } else {
                container.setMilli(0);
            }
        } catch (IllegalArgumentException e) {
            notTime();
        }
    }

    private static void notTime() {
        throw new FixDecoderException("Not a UTCTimeOnly");
    }

}
