/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InboundFixMessageDecoderTest {

    private static final FixDecoderConfig CONFIG = FixDecoderConfig.newBuilder()
        .setMaxBodyLength(16)
        .build();

    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        channel = new EmbeddedChannel(new InboundFixMessageDecoder(CONFIG));
    }

    @AfterEach
    void tearDown() {
        channel.finish();
    }

    @Test
    void empty() {
        assertTrue(decode("").isEmpty());
    }

    @Test
    void partialWithBeginStringTag() {
        assertTrue(decode("8").isEmpty());
    }

    @Test
    void partialWithBeginStringTagEquals() {
        assertTrue(decode("8=").isEmpty());
    }

    @Test
    void partialWithBeginStringTagEqualsValue() {
        assertTrue(decode("8=FIX.4.2").isEmpty());
    }

    @Test
    void partialWithBeginStringTagEqualsValueSoh() {
        assertTrue(decode("8=FIX.4.2\u0001").isEmpty());
    }

    @Test
    void partialWithBodyLengthTag() {
        assertTrue(decode("8=FIX.4.2\u00019").isEmpty());
    }

    @Test
    void partialWithBodyLengthTagEquals() {
        assertTrue(decode("8=FIX.4.2\u00019=").isEmpty());
    }

    @Test
    void partialWithBodyLengthTagEqualsValue() {
        assertTrue(decode("8=FIX.4.2\u00019=0").isEmpty());
    }

    @Test
    void partialWithBodyLengthTagEqualsValueSoh() {
        assertTrue(decode("8=FIX.4.2\u00019=0\u0001").isEmpty());
    }

    @Test
    void partials() {
        assertTrue(decode("8=FIX.4.2\u0001").isEmpty());

        var messages = decode("9=0\u000110=198\u0001");

        assertEquals(1, messages.size());

        var message = messages.get(0);

        assertFalse(message.isGarbled());
        assertEquals(FIX_4_2, message.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001"), message.content());
        assertEquals(14, message.bodyOffset());
        assertEquals(0, message.bodyLength());
        assertEquals(198, message.checkSum());
    }

    @Test
    void emptyBody() {
        var messages = decode("8=FIX.4.2\u00019=0\u000110=198\u0001");

        assertEquals(1, messages.size());

        var message = messages.get(0);

        assertFalse(message.isGarbled());
        assertEquals(FIX_4_2, message.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001"), message.content());
        assertEquals(14, message.bodyOffset());
        assertEquals(0, message.bodyLength());
        assertEquals(198, message.checkSum());
    }

    @Test
    void nonEmptyBody() {
        var messages = decode("8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001");

        assertEquals(1, messages.size());

        var message = messages.get(0);

        assertFalse(message.isGarbled());
        assertEquals(FIX_4_2, message.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001"), message.content());
        assertEquals(14, message.bodyOffset());
        assertEquals(5, message.bodyLength());
        assertEquals(181, message.checkSum());
    }

    @Test
    void multiple() {
        var messages = decode("8=FIX.4.2\u00019=0\u000110=198\u00018=FIX.4.2\u00019=0\u000110=198\u0001");

        assertEquals(2, messages.size());

        for (var message : messages) {
            assertFalse(message.isGarbled());
            assertEquals(FIX_4_2, message.version());
            assertEquals(copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001"), message.content());
            assertEquals(14, message.bodyOffset());
            assertEquals(0, message.bodyLength());
            assertEquals(198, message.checkSum());
        }
    }

    @Test
    void garbledAtStart() {
        var messages = decode("garbled\u00018=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001");

        assertEquals(2, messages.size());

        var garbledMessage = messages.get(0);

        assertTrue(garbledMessage.isGarbled());
        assertEquals(null, garbledMessage.version());
        assertEquals(copiedBuffer("garbled\u0001"), garbledMessage.content());
        assertEquals(-1, garbledMessage.bodyOffset());
        assertEquals(-1, garbledMessage.checkSum());

        var message = messages.get(1);

        assertFalse(message.isGarbled());
        assertEquals(FIX_4_2, message.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001"), message.content());
        assertEquals(14, message.bodyOffset());
        assertEquals(5, message.bodyLength());
        assertEquals(181, message.checkSum());
    }

    @Test
    void garbledInBetween() {
        var messages = decode("8=FIX.4.2\u00019=0\u000110=198\u0001garbled\u00018=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001");

        assertEquals(3, messages.size());

        var firstMessage = messages.get(0);

        assertFalse(firstMessage.isGarbled());
        assertEquals(FIX_4_2, firstMessage.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001"), firstMessage.content());
        assertEquals(14, firstMessage.bodyOffset());
        assertEquals(0, firstMessage.bodyLength());
        assertEquals(198, firstMessage.checkSum());

        var garbledMessage = messages.get(1);

        assertTrue(garbledMessage.isGarbled());
        assertEquals(null, garbledMessage.version());
        assertEquals(copiedBuffer("garbled\u0001"), garbledMessage.content());
        assertEquals(-1, garbledMessage.bodyOffset());
        assertEquals(-1, garbledMessage.bodyLength());
        assertEquals(-1, garbledMessage.checkSum());

        var secondMessage = messages.get(2);

        assertFalse(secondMessage.isGarbled());
        assertEquals(FIX_4_2, secondMessage.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001"), secondMessage.content());
        assertEquals(14, secondMessage.bodyOffset());
        assertEquals(5, secondMessage.bodyLength());
        assertEquals(181, secondMessage.checkSum());
    }

    @Test
    void garbledAtEnd() {
        var messages = decode("8=FIX.4.2\u00019=0\u000110=198\u0001garbled\u0001");

        assertEquals(1, messages.size());

        var message = messages.get(0);

        assertFalse(message.isGarbled());
        assertEquals(FIX_4_2, message.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001"), message.content());
        assertEquals(14, message.bodyOffset());
        assertEquals(0, message.bodyLength());
        assertEquals(198, message.checkSum());
    }

    @Test
    void maxLengthMessage() {
        var messages = decode("8=FIX.4.2\u00019=16\u000149=aaaaaaaaaaaa\u000110=052\u0001");

        assertEquals(1, messages.size());

        var message = messages.get(0);

        assertFalse(message.isGarbled());
        assertEquals(FIX_4_2, message.version());
        assertEquals(copiedBuffer("8=FIX.4.2\u00019=16\u000149=aaaaaaaaaaaa\u000110=052\u0001"), message.content());
        assertEquals(15, message.bodyOffset());
        assertEquals(16, message.bodyLength());
        assertEquals(52, message.checkSum());
    }

    @Test
    void tooLongMessage() {
        assertThrows(TooLongFixMessageException.class, () -> decode("8=FIX.4.2\u00019=17\u000149=aaaaaaaaaaaaa\u000110=149\u0001"));
    }

    private List<InboundFixMessage> decode(final String bytes) {
        var buffer = copiedBuffer(bytes);

        channel.writeInbound(buffer);

        var messages = new ArrayList<InboundFixMessage>();

        while (true) {
            InboundFixMessage message = channel.readInbound();
            if (message == null)
                break;

            messages.add(message);
        }

        return messages;
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

}
