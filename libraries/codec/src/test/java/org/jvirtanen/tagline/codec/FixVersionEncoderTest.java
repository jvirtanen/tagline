/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixVersionEncoderTest {

    @Test
    void bits() {
        assertEquals("FIX.4.2\u0001", encode(FIX_4_2));
    }

    @Test
    void bytes() {
        assertEquals("FIXT.1.1\u0001", encode(FIXT_1_1));
    }

    private static String encode(final FixVersion value) {
        var buffer = Unpooled.buffer();

        FixVersionEncoder.encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
