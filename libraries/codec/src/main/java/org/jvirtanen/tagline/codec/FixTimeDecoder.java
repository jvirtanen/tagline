/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixTimeDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixTime container) {
        if (length != 8 && length != 12)
            notTime();

        try {
            container.setHour(decodeTwoDigits(bytes, index));

            if (bytes[index + 2] != ':')
                notTime();

            container.setMinute(decodeTwoDigits(bytes, index + 3));

            if (bytes[index + 5] != ':')
                notTime();

            container.setSecond(decodeTwoDigits(bytes, index + 6));

            if (length == 12) {
                if (bytes[index + 8] != '.')
                    notTime();

                container.setMilli(decodeThreeDigits(bytes, index + 9));
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
