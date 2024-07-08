/*
 * Copyright 2024 Jussi Virtanen
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
        var bytes = value.getBytes(ISO_8859_1);

        FixDateDecoder.decode(bytes, bytes.length, container);
    }

    private static void assertNotDate(final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value, new DefaultFixTimestamp()));

        assertEquals("Not a UTCDate or LocalMktDate", exception.getMessage());
    }

}
