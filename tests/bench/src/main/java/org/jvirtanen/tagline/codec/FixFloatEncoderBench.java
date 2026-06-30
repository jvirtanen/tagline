/*
 * Copyright 2024 Jussi Virtanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixFloatEncoderBench extends Bench {

    private ByteBuf buffer;

    private FixFloat container;
    private FixFloatEncoder encoder;

    @Setup(Level.Trial)
    public void setUp() {
        buffer = Unpooled.directBuffer(1024);

        container = new DefaultFixFloat();
        encoder = new FixFloatEncoder();
    }

    @Benchmark
    public ByteBuf encodeOneIntegerDigit() {
        return encode(1, 0);
    }

    @Benchmark
    public ByteBuf encodeTwoIntegerDigits() {
        return encode(12, 0);
    }

    @Benchmark
    public ByteBuf encodeThreeIntegerDigits() {
        return encode(123, 0);
    }

    @Benchmark
    public ByteBuf encodeFourIntegerDigits() {
        return encode(1234, 0);
    }

    @Benchmark
    public ByteBuf encodeOneIntegerDigitAndOneFractionalDigit() {
        return encode(12, 1);
    }

    @Benchmark
    public ByteBuf encodeOneIntegerDigitAndTwoFractionalDigits() {
        return encode(123, 2);
    }

    @Benchmark
    public ByteBuf encodeOneIntegerDigitAndThreeFractionalDigits() {
        return encode(1234, 3);
    }

    @Benchmark
    public ByteBuf encodeOneIntegerDigitAndFourFractionalDigits() {
        return encode(12345, 4);
    }

    @Benchmark
    public ByteBuf encodeMinPositiveValue() {
        return encode(1, 18);
    }

    @Benchmark
    public ByteBuf encodeMaxValue() {
        return encode(Long.MAX_VALUE, 0);
    }

    private ByteBuf encode(final long unscaledValue, final int scale) {
        buffer.clear();

        container.setValue(unscaledValue, scale);
        encoder.encode(container, buffer);

        return buffer;
    }

}
