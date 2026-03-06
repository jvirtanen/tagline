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

class FixCharDecoderTest {

    @Test
    void ascii() {
        assertEquals('A', decode("A"));
    }

    @Test
    void iso88591() {
        assertEquals('£', decode("£"));
    }

    @Test
    void invalidLength() {
        assertNotChar("AA");
    }

    private static char decode(final String value) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);

        return FixCharDecoder.decode(bytes, 1, bytes.length - 2);
    }

    private static void assertNotChar(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals("Not a Char", exception.getMessage());
    }

}
