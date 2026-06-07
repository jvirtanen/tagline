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

class FixTimeDecoderTest {

    private FixTime container;

    @BeforeEach
    void setUp() {
        container = new DefaultFixTimestamp();
    }

    @Test
    void valueWithSeconds() {
        decode("22:10:36", container);

        assertEquals(22, container.hour());
        assertEquals(10, container.minute());
        assertEquals(36, container.second());
        assertEquals(0, container.milli());
    }

    @Test
    void valueWithMilliseconds() {
        decode("22:10:36.250", container);

        assertEquals(22, container.hour());
        assertEquals(10, container.minute());
        assertEquals(36, container.second());
        assertEquals(250, container.milli());
    }

    @Test
    void invalidLength() {
        assertNotTime("22:10");
    }

    @Test
    void invalidByteAtFirstColon() {
        assertNotTime("22;10:36");
    }

    @Test
    void invalidByteAtSecondColon() {
        assertNotTime("22:10;36");
    }

    @Test
    void invalidByteAtPeriod() {
        assertNotTime("22:10:36,250");
    }

    @Test
    void invalidComponent() {
        assertNotTime("22:10:66");
    }

    private static void decode(final String value, final FixTime container) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);

        FixTimeDecoder.decode(bytes, 1, bytes.length - 2, container);
    }

    private static void assertNotTime(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value, new DefaultFixTimestamp()));

        assertEquals("Not a UTCTimeOnly", exception.getMessage());
    }

}
