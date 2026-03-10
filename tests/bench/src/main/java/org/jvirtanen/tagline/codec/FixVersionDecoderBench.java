/*
 * Copyright 2026 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixVersionDecoderBench extends Bench {

    private ByteBuf fix42;
    private ByteBuf fixt11;

    private FixVersionDecoder decoder;

    @Setup(Level.Iteration)
    public void setUp() {
        fix42 = buffer("FIX.4.2\u0001");
        fixt11 = buffer("FIXT.1.1\u0001");

        decoder = new FixVersionDecoder();
    }

    @Benchmark
    public int decodeFix42() {
        return decoder.decode(fix42, 0, 8);
    }

    @Benchmark
    public int decodeFixt11() {
        return decoder.decode(fixt11, 0, 9);
    }

    private static ByteBuf buffer(final String value) {
        var buffer = Unpooled.directBuffer(value.length());

        buffer.writeBytes(value.getBytes(ISO_8859_1));

        return buffer;
    }

}
