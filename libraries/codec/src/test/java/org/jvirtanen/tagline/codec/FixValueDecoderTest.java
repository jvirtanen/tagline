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

class FixValueDecoderTest {

    @Test
    void empty() {
        assertEquals(1, decode("\u0001"));
    }

    @Test
    void oneByte() {
        assertEquals(2, decode("a\u0001"));
    }

    @Test
    void twoBytes() {
        assertEquals(3, decode("ab\u0001"));
    }

    private long decode(final String value) {
        var bytes = value.getBytes(ISO_8859_1);

        return FixValueDecoder.decode(bytes, 0, bytes.length);
    }

}
