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

import org.junit.jupiter.api.Test;

class FixBooleanDecoderTest {

    @Test
    void yes() {
        assertTrue(decode("Y"));
    }

    @Test
    void no() {
        assertFalse(decode("N"));
    }

    @Test
    void invalidLength() {
        assertNotBoolean("Yes");
    }

    @Test
    void invalidByte() {
        assertNotBoolean("T");
    }

    private static boolean decode(final String value) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);

        return FixBooleanDecoder.decode(bytes, 1, bytes.length - 2);
    }

    private static void assertNotBoolean(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a Boolean", exception.getMessage());
    }

}
