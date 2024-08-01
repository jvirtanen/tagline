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

class FixCheckSumCalculatorTest {

    @Test
    void calculate() {
        var buffer = copiedBuffer("8=FIX.4.2\u00019=35\u000135=D\u000149=initiator\u000156=acceptor\u000134=5\u000110=058\u0001");

        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 15, 35, 0);

        var checkSum = new FixCheckSumCalculator();

        assertEquals(58, checkSum.calculate(message));
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

}
