/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class DefaultInboundFixMessageTest {

    @Test
    void setGarbled() {
        var content = copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, content, 14, 0, 198);

        message.setGarbled();

        assertTrue(message.isGarbled());
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

}
