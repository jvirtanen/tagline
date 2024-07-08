/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixBooleanEncoderTest {

    @Test
    void yes() {
        assertEquals("Y\u0001", encode(true));
    }

    @Test
    void no() {
        assertEquals("N\u0001", encode(false));
    }

    private static String encode(final boolean value) {
        var buffer = Unpooled.buffer();

        FixBooleanEncoder.encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
