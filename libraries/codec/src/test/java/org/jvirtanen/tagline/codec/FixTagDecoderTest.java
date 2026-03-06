/*
 * Copyright 2024 Jussi Virtanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

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
        var bytes = value.getBytes(ISO_8859_1);

        return decoder.decode(bytes, 0, bytes.length);
    }

    private void assertInvalidTag(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Invalid tag", exception.getMessage());
    }

}
