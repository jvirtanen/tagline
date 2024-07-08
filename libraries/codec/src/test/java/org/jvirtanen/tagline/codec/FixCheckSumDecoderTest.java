/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixCheckSumDecoderTest {

    @Test
    void valid() {
        assertEquals(123, decode("123\u0001"));
    }

    @Test
    void character() {
        decodeInvalid("12c\u0001");
    }

    @Test
    void tooShortValue() {
        decodeInvalid("12\u00018");
    }

    @Test
    void tooLongValue() {
        decodeInvalid("1234\u0001");
    }

    private static long decode(final String value) {
        var buffer = Unpooled.copiedBuffer(value, ISO_8859_1);

        return FixCheckSumDecoder.decode(buffer, buffer.readerIndex());
    }

    private static void decodeInvalid(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Invalid CheckSum(10) value", exception.getMessage());
    }

}
