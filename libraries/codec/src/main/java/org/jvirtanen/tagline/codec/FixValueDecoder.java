/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixValueDecoder {

    static int decode(final byte[] bytes, int offset, final int length) {
        while (offset < length) {
            if (bytes[offset] == SOH)
                return offset + 1;

            offset++;
        }

        return -1;
    }

}
