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

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixCheckSumDecoderTest {

    @Test
    void valid() {
        assertEquals(123, decode("123\u0001"));
    }

    @Test
    void tooLowByte() {
        decodeInvalid("/23\u0001");
    }

    @Test
    void tooHighByte() {
        decodeInvalid("12:\u0001");
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
