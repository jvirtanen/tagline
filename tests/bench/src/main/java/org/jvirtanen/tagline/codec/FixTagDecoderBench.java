/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixTagDecoderBench extends Bench {

    private ByteBuf oneDigitBuffer;
    private ByteBuf twoDigitBuffer;
    private ByteBuf threeDigitBuffer;
    private ByteBuf fourDigitBuffer;
    private ByteBuf fiveDigitBuffer;

    private FixTagDecoder decoder;

    @Setup(Level.Iteration)
    public void setUp() {
        oneDigitBuffer = directBuffer("1=");
        twoDigitBuffer = directBuffer("12=");
        threeDigitBuffer = directBuffer("123=");
        fourDigitBuffer = directBuffer("1234=");
        fiveDigitBuffer = directBuffer("12345=");

        decoder = new FixTagDecoder();
    }

    @Benchmark
    public int decodeOneDigit() {
        return decode(oneDigitBuffer);
    }

    @Benchmark
    public int decodeTwoDigits() {
        return decode(twoDigitBuffer);
    }

    @Benchmark
    public int decodeThreeDigits() {
        return decode(threeDigitBuffer);
    }

    @Benchmark
    public int decodeFourDigits() {
        return decode(fourDigitBuffer);
    }

    @Benchmark
    public int decodeFiveDigits() {
        return decode(fiveDigitBuffer);
    }

    private int decode(final ByteBuf buffer) {
        decoder.decode(buffer, 0, buffer.readableBytes());

        return decoder.tag();
    }

    private static final ByteBuf directBuffer(final String value) {
        var buffer = Unpooled.directBuffer(64);

        buffer.writeBytes(value.getBytes(ISO_8859_1));

        return buffer;
    }

}
