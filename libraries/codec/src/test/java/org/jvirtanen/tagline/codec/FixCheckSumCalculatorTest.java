/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class FixCheckSumCalculatorTest {

    @Test
    void calculate() {
        var buffer = Unpooled.copiedBuffer("8=FIX.4.2\u00019=24\u000135=D\u000149=initiator\u000156=acceptor\u000134=5\u0001", ISO_8859_1);

        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 0);

        var calculator = new FixCheckSumCalculator();

        assertEquals(56, calculator.calculate(message));
    }

}
