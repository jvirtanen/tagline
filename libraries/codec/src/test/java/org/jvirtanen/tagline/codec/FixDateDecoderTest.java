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

class FixDateDecoderTest {

    private FixDate container;

    @BeforeEach
    void setUp() {
        container = new DefaultFixTimestamp();
    }

    @Test
    void value() {
        decode("20240107", container);

        assertEquals(2024, container.year());
        assertEquals(1, container.month());
        assertEquals(7, container.day());
    }

    @Test
    void invalidLength() {
        assertNotDate("202401");
    }

    @Test
    void invalidYear() {
        assertNotDate("MMXX0107");
    }

    @Test
    void invalidMonth() {
        assertNotDate("20240007");
    }

    @Test
    void invalidDay() {
        assertNotDate("20240132");
    }

    private static void decode(final String value, final FixDate container) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);

        FixDateDecoder.decode(bytes, 1, bytes.length - 2, container);
    }

    private static void assertNotDate(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value, new DefaultFixTimestamp()));

        assertEquals("Not a UTCDate or LocalMktDate", exception.getMessage());
    }

}
