/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixTimeFormat.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultOutboundFixMessageTest {

    private ByteBuf content;
    private DefaultOutboundFixMessage message;

    @BeforeEach
    void setUp() {
        content = Unpooled.buffer();
        message = new DefaultOutboundFixMessage(FixVersion.FIX_4_2, content);
    }

    @Test
    void addYes() {
        message.addBoolean(141, true);

        assertEquals("141=Y\u0001", content());
    }

    @Test
    void addNo() {
        message.addBoolean(141, false);

        assertEquals("141=N\u0001", content());
    }

    @Test
    void addChar() {
        message.addChar(18, '6');

        assertEquals("18=6\u0001", content());
    }

    @Test
    void addPositiveInt() {
        message.addInt(34, 5);

        assertEquals("34=5\u0001", content());
    }

    @Test
    void addNegativeInt() {
        message.addInt(34, -5);

        assertEquals("34=-5\u0001", content());
    }

    @Test
    void addPositiveFloatWithObject() {
        message.addFloat(211, new DefaultFixFloat(5, 1));

        assertEquals("211=0.5\u0001", content());
    }

    @Test
    void addPositiveFloatWithDouble() {
        message.addFloat(211, 0.5, 1);

        assertEquals("211=0.5\u0001", content());
    }

    @Test
    void addNegativeFloatWithObject() {
        message.addFloat(211, new DefaultFixFloat(-5, 1));

        assertEquals("211=-0.5\u0001", content());
    }

    @Test
    void addNegativeFloatWithDouble() {
        message.addFloat(211, -0.5, 1);

        assertEquals("211=-0.5\u0001", content());
    }

    @Test
    void addAsciiString() {
        message.addString(49, "initiator");

        assertEquals("49=initiator\u0001", content());
    }

    @Test
    void addIso88591String() {
        message.addString(15, "£");

        assertEquals("15=£\u0001", content());
    }

    @Test
    void addUnicodeString() {
        message.addString(15, "₹");

        assertEquals("15=?\u0001", content());
    }

    @Test
    void addDate() {
        message.addDate(51, new DefaultFixTimestamp(2024, 1, 7));

        assertEquals("51=20240107\u0001", content());
    }

    @Test
    void addTimeWithoutSeconds() {
        message.addTime(273, new DefaultFixTimestamp(16, 44, 30, 950), WITH_SECONDS);

        assertEquals("273=16:44:30\u0001", content());
    }

    @Test
    void addTimeWithMilliseconds() {
        message.addTime(273, new DefaultFixTimestamp(16, 44, 30, 950), WITH_MILLISECONDS);

        assertEquals("273=16:44:30.950\u0001", content());
    }

    @Test
    void addTimestampObjectWithSeconds() {
        message.addTimestamp(52, new DefaultFixTimestamp(2024, 1, 7, 16, 44, 30, 950), WITH_SECONDS);

        assertEquals("52=20240107-16:44:30\u0001", content());
    }

    @Test
    void addTimestampObjectWithMilliseconds() {
        message.addTimestamp(52, new DefaultFixTimestamp(2024, 1, 7, 16, 44, 30, 950), WITH_MILLISECONDS);

        assertEquals("52=20240107-16:44:30.950\u0001", content());
    }

    @Test
    void addTimestampLongWithSeconds() {
        message.addTimestamp(52, 1704645870950l, WITH_SECONDS);

        assertEquals("52=20240107-16:44:30\u0001", content());
    }

    @Test
    void addTimestampLongWithMilliseconds() {
        message.addTimestamp(52, 1704645870950l, WITH_MILLISECONDS);

        assertEquals("52=20240107-16:44:30.950\u0001", content());
    }

    @Test
    void addBytes() {
        message.addBytes(49, new byte[] { 'i', 'n', 'i', 't', 'i', 'a', 't', 'o', 'r', });

        assertEquals("49=initiator\u0001", content());
    }

    private String content() {
        return content.toString(ISO_8859_1);
    }

}
