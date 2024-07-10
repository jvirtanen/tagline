/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixTagDecoderTest {

    private FixTagDecoder decoder;

    @BeforeEach
    void setUp() {
        decoder = new FixTagDecoder();
    }

    @Test
    void oneDigit() {
        assertEquals(2, decode("1="));
        assertEquals(1, decoder.tag());
    }

    @Test
    void twoDigits() {
        assertEquals(3, decode("12="));
        assertEquals(12, decoder.tag());
    }

    @Test
    void maxValue() {
        var asString = Integer.toString(Integer.MAX_VALUE);

        assertEquals(asString.length() + 1, decode(String.format("%s=", asString)));
        assertEquals(Integer.MAX_VALUE, decoder.tag());
    }

    @Test
    void missingTag() {
        assertInvalidTag("=");
    }

    @Test
    void zero() {
        assertInvalidTag("0=");
    }

    @Test
    void leadingZero() {
        assertInvalidTag("01=");
    }

    @Test
    void tooLargeTag() {
        assertInvalidTag("2147483648=");
    }

    @Test
    void invalidByte() {
        assertInvalidTag("foo=");
    }

    private int decode(final String value) {
        var buffer = Unpooled.copiedBuffer(value, ISO_8859_1);

        return decoder.decode(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    private void assertInvalidTag(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Invalid tag", exception.getMessage());
    }

}
