/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixDateEncoderTest {

    @Test
    void value() {
        assertEquals("20240107", encode(new DefaultFixTimestamp(2024, 1, 7)));
    }

    private static String encode(final FixDate value) {
        var buffer = Unpooled.buffer();

        FixDateEncoder.encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
