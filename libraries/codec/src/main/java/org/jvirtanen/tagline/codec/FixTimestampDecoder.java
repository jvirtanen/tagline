/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixTimestampDecoder {

    static void decode(final byte[] bytes, final int length, final FixTimestamp container) {
        if (length != 17 && length != 21)
            notTimestamp();

        try {
            container
                .setYear(decodeFourDigits(bytes, 0))
                .setMonth(decodeTwoDigits(bytes, 4))
                .setDay(decodeTwoDigits(bytes, 6));

            if (bytes[8] != '-')
                notTimestamp();

            container.setHour(decodeTwoDigits(bytes, 9));

            if (bytes[11] != ':')
                notTimestamp();

            container.setMinute(decodeTwoDigits(bytes, 12));

            if (bytes[14] != ':')
                notTimestamp();

            container.setSecond(decodeTwoDigits(bytes, 15));

            if (length == 21) {
                if (bytes[17] != '.')
                    notTimestamp();

                container.setMilli(decodeThreeDigits(bytes, 18));
            } else {
                container.setMilli(0);
            }
        } catch (IllegalArgumentException e) {
            notTimestamp();
        }
    }

    private static void notTimestamp() {
        throw new FixDecoderException("Not a UTCTimestamp");
    }

}
