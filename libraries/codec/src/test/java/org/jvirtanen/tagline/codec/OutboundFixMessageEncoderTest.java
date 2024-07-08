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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutboundFixMessageEncoderTest {

    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        channel = new EmbeddedChannel(new OutboundFixMessageEncoder());
    }

    @AfterEach
    void tearDown() {
        channel.finish();
    }

    @Test
    void empty() {
        var message = new DefaultOutboundFixMessage(FIX_4_2, Unpooled.buffer());

        channel.writeOutbound(message);

        assertOutbound("8=FIX.4.2\u00019=0\u0001");
        assertOutbound("10=198\u0001");
    }

    @Test
    void nonEmpty() {
        var message = new DefaultOutboundFixMessage(FIX_4_2, Unpooled.buffer());

        message.addChar(35, 'D');

        channel.writeOutbound(message);

        assertOutbound("8=FIX.4.2\u00019=5\u0001");
        assertOutbound("35=D\u000110=181\u0001");
    }

    private void assertOutbound(final String expected) {
        ByteBuf actual = channel.readOutbound();

        assertEquals(expected, actual.toString(ISO_8859_1));
    }

}
