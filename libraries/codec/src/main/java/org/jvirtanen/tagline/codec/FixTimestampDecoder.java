/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixTimestampDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixTimestamp container) {
        if (length != 17 && length != 21)
            notTimestamp();

        try {
            container
                .setYear(decodeFourDigits(bytes, index))
                .setMonth(decodeTwoDigits(bytes, index + 4))
                .setDay(decodeTwoDigits(bytes, index + 6));

            if (bytes[index + 8] != '-')
                notTimestamp();

            container.setHour(decodeTwoDigits(bytes, index + 9));

            if (bytes[index + 11] != ':')
                notTimestamp();

            container.setMinute(decodeTwoDigits(bytes, index + 12));

            if (bytes[index + 14] != ':')
                notTimestamp();

            container.setSecond(decodeTwoDigits(bytes, index + 15));

            if (length == 21) {
                if (bytes[index + 17] != '.')
                    notTimestamp();

                container.setMilli(decodeThreeDigits(bytes, index + 18));
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
