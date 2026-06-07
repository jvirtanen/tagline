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

class FixBooleanEncoderTest {

    @Test
    void yes() {
        assertEquals("Y\u0001", encode(true));
    }

    @Test
    void no() {
        assertEquals("N\u0001", encode(false));
    }

    private static String encode(final boolean value) {
        var buffer = Unpooled.buffer();

        FixBooleanEncoder.encode(value, buffer);

        return buffer.toString(ISO_8859_1);
    }

}
