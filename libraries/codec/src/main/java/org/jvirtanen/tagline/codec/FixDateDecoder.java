/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;

class FixDateDecoder {

    static void decode(final byte[] bytes, final int length, final FixDate container) {
        if (length != 8)
            notDate();

        try {
            container
                .setYear(decodeFourDigits(bytes, 0))
                .setMonth(decodeTwoDigits(bytes, 4))
                .setDay(decodeTwoDigits(bytes, 6));
        } catch (IllegalArgumentException e) {
            notDate();
        }
    }

    private static void notDate() {
        throw new FixDecoderException("Not a UTCDate or LocalMktDate");
    }

}
