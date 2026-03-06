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

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixMediumEncoderBench extends Bench {

    private ByteBuf buffer;

    @Setup(Level.Iteration)
    public void setUp() {
        buffer = Unpooled.directBuffer(1024);
    }

    @Benchmark
    public ByteBuf encodeOneDigit() {
        return encode(1);
    }

    @Benchmark
    public ByteBuf encodeTwoDigits() {
        return encode(12);
    }

    @Benchmark
    public ByteBuf encodeThreeDigits() {
        return encode(123);
    }

    @Benchmark
    public ByteBuf encodeFourDigits() {
        return encode(1234);
    }

    @Benchmark
    public ByteBuf encodeFiveDigits() {
        return encode(12345);
    }

    @Benchmark
    public ByteBuf encodeSixDigits() {
        return encode(123456);
    }

    @Benchmark
    public ByteBuf encodeSevenDigits() {
        return encode(1234567);
    }

    private ByteBuf encode(final int value) {
        buffer.clear();

        FixMediumEncoder.encode(value, EQUALS, buffer);

        return buffer;
    }

}
