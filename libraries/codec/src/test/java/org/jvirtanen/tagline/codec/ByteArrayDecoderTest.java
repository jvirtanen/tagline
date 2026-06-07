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
import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ByteArrayDecoderTest {

    @Test
    void digit() {
        assertEquals(1, decodeDigit(getBytes("1"), 0));
    }

    @Test
    void twoDigits() {
        assertEquals(1, decodeTwoDigits(getBytes("20240107"), 4));
    }

    @Test
    void threeDigits() {
        assertEquals(950, decodeThreeDigits(getBytes("16:44:30.950"), 9));
    }

    @Test
    void fourDigits() {
        assertEquals(2024, decodeFourDigits(getBytes("20240107"), 0));
    }

    @Test
    void invalidByte() {
        assertNotDigit(() -> decodeDigit(getBytes("a"), 0));
    }

    private static byte[] getBytes(final String value) {
        return value.getBytes(ISO_8859_1);
    }

    private static void assertNotDigit(final Executable executable) {
        var exception = assertThrows(IllegalArgumentException.class, executable);

        assertEquals("Not a digit", exception.getMessage());
    }

}

