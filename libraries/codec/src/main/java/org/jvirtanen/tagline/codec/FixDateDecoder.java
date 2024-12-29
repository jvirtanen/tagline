/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;

class FixDateDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixDate container) {
        if (length != 8)
            notDate();

        try {
            container
                .setYear(decodeFourDigits(bytes, index))
                .setMonth(decodeTwoDigits(bytes, index + 4))
                .setDay(decodeTwoDigits(bytes, index + 6));
        } catch (IllegalArgumentException e) {
            notDate();
        }
    }

    private static void notDate() {
        throw new FixDecoderException("Not a UTCDate or LocalMktDate");
    }

}
