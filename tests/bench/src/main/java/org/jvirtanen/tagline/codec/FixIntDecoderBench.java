/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixIntDecoderBench extends Bench {

    private byte[] oneDigitBytes;
    private byte[] twoDigitBytes;
    private byte[] threeDigitBytes;
    private byte[] fourDigitBytes;
    private byte[] fiveDigitBytes;
    private byte[] sixDigitBytes;
    private byte[] sevenDigitBytes;
    private byte[] largeValueBytes;
    private byte[] maxValueBytes;

    @Setup(Level.Iteration)
    public void setUp() {
        oneDigitBytes = getBytes(1);
        twoDigitBytes = getBytes(12);
        threeDigitBytes = getBytes(123);
        fourDigitBytes = getBytes(1234);
        fiveDigitBytes = getBytes(12345);
        sixDigitBytes = getBytes(123456);
        sevenDigitBytes = getBytes(1234567);
        largeValueBytes = getBytes(Long.MAX_VALUE / 10);
        maxValueBytes = getBytes(Long.MAX_VALUE);
    }

    @Benchmark
    public long decodeOneDigit() {
        return decode(oneDigitBytes);
    }

    @Benchmark
    public long decodeTwoDigits() {
        return decode(twoDigitBytes);
    }

    @Benchmark
    public long decodeThreeDigits() {
        return decode(threeDigitBytes);
    }

    @Benchmark
    public long decodeFourDigits() {
        return decode(fourDigitBytes);
    }

    @Benchmark
    public long decodeFiveDigits() {
        return decode(fiveDigitBytes);
    }

    @Benchmark
    public long decodeSixDigits() {
        return decode(sixDigitBytes);
    }

    @Benchmark
    public long decodeSevenDigits() {
        return decode(sevenDigitBytes);
    }

    @Benchmark
    public long decodeLargeValue() {
        return decode(largeValueBytes);
    }

    @Benchmark
    public long decodeMaxValue() {
        return decode(maxValueBytes);
    }

    private long decode(final byte[] bytes) {
        return FixIntDecoder.decode(bytes, bytes.length);
    }

    private static final byte[] getBytes(final long value) {
        return Long.toString(value).getBytes(ISO_8859_1);
    }

}
