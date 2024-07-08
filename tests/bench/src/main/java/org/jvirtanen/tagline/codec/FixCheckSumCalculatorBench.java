/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Random;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixCheckSumCalculatorBench extends Bench {

    private static final Random RANDOM = new Random();

    private ByteBuf buffer;

    private FixCheckSumCalculator calculator;

    @Setup(Level.Iteration)
    public void setUp() {
        var bytes = new byte[256];

        RANDOM.nextBytes(bytes);

        buffer = Unpooled.directBuffer(bytes.length);
        buffer.writeBytes(bytes);
        buffer.markReaderIndex();

        calculator = new FixCheckSumCalculator();
    }

    @Benchmark
    public int calculateCheckSum() {
        buffer.resetReaderIndex();

        return calculator.calculate(buffer);
    }

}
