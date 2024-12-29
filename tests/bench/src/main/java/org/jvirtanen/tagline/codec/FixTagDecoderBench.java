/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixTagDecoderBench extends Bench {

    private byte[] oneDigit;
    private byte[] twoDigits;
    private byte[] threeDigits;
    private byte[] fourDigits;
    private byte[] fiveDigits;

    private FixTagDecoder decoder;

    @Setup(Level.Iteration)
    public void setUp() {
        oneDigit = getBytes("1=");
        twoDigits = getBytes("12=");
        threeDigits = getBytes("123=");
        fourDigits = getBytes("1234=");
        fiveDigits = getBytes("12345=");

        decoder = new FixTagDecoder();
    }

    @Benchmark
    public int decodeOneDigit() {
        return decode(oneDigit);
    }

    @Benchmark
    public int decodeTwoDigits() {
        return decode(twoDigits);
    }

    @Benchmark
    public int decodeThreeDigits() {
        return decode(threeDigits);
    }

    @Benchmark
    public int decodeFourDigits() {
        return decode(fourDigits);
    }

    @Benchmark
    public int decodeFiveDigits() {
        return decode(fiveDigits);
    }

    private int decode(final byte[] bytes) {
        decoder.decode(bytes, 0, bytes.length);

        return decoder.tag();
    }

    private static byte[] getBytes(final String value) {
        return value.getBytes(ISO_8859_1);
    }

}
