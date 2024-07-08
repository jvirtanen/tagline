/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixBooleanDecoder {

    static boolean decode(final byte[] bytes, final int length) {
        if (length != 1)
            notBoolean();

        byte value = bytes[0];

        if (value == YES)
            return true;

        if (value != NO)
            notBoolean();

        return false;
    }

    private static void notBoolean() {
        throw new FixDecoderException("Not a Boolean");
    }

}
