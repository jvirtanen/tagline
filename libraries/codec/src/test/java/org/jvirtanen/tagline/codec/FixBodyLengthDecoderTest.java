/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixBodyLengthDecoderTest {

    private FixBodyLengthDecoder decoder;

    @BeforeEach
    void setUp() {
        decoder = new FixBodyLengthDecoder();
    }

    @Test
    void empty() {
        assertEquals(-1, decode(""));

        assertEquals(0, decoder.bodyLength());
    }

    @Test
    void partial() {
        assertEquals(-1, decode("0"));

        assertEquals(0, decoder.bodyLength());
    }

    @Test
    void zero() {
        assertEquals(2, decode("0\u0001"));

        assertEquals(0, decoder.bodyLength());
    }

    @Test
    void oneDigit() {
        assertEquals(2, decode("1\u0001"));

        assertEquals(1, decoder.bodyLength());
    }

    @Test
    void twoDigits() {
        assertEquals(3, decode("12\u0001"));

        assertEquals(12, decoder.bodyLength());
    }

    @Test
    void threeDigits() {
        assertEquals(4, decode("123\u0001"));

        assertEquals(123, decoder.bodyLength());
    }

    @Test
    void maxValue() {
        assertEquals(11, decode(String.format("%s\u0001", Integer.MAX_VALUE)));

        assertEquals(Integer.MAX_VALUE, decoder.bodyLength());
    }

    @Test
    void missing() {
        notBodyLength("\u0001");
    }

    @Test
    void negative() {
        notBodyLength("-1\u0001");
    }

    @Test
    void overflow() {
        long value = Integer.MAX_VALUE + 1;

        notBodyLength(String.format("%s\u0001", value));
    }

    private long decode(final String value) {
        var buffer = Unpooled.copiedBuffer(value, ISO_8859_1);

        return decoder.decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    private void notBodyLength(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Invalid BodyLength(9) value", exception.getMessage());
    }

}
