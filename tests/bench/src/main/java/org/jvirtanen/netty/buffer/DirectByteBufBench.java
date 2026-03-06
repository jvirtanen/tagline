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
package org.jvirtanen.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import java.util.Arrays;
import java.util.Random;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class DirectByteBufBench extends Bench {

    private static final Random RANDOM = new Random();

    private static final int SIZE = 256;

    private static final byte ZERO = 0;

    private ByteBuf buffer;
    private byte[] bytes;

    private long sum;

    private ByteProcessor calculator;

    @Setup(Level.Iteration)
    public void setUp() {
        bytes = new byte[SIZE];

        RANDOM.nextBytes(bytes);

        buffer = Unpooled.directBuffer(bytes.length);
        buffer.writeBytes(bytes);

        Arrays.fill(bytes, ZERO);

        calculator = new SumCalculator();
    }

    @Benchmark
    public long getBytes() {
        buffer.getBytes(0, bytes);

        sum = 0;

        for (byte b : bytes)
            sum += b;

        return sum;
    }

    @Benchmark
    public long getByte() {
        sum = 0;

        for (int i = 0; i < SIZE; i++)
            sum += buffer.getByte(i);

        return sum;
    }

    @Benchmark
    public long getShort() {
        sum = 0;

        for (int i = 0; i < SIZE; i += 2) {
            short bits = buffer.getShort(i);

            sum += bits >> 8;
            sum += bits & 0xff;
        }

        return sum;
    }

    @Benchmark
    public long getInt() {
        sum = 0;

        for (int i = 0; i < SIZE; i += 4) {
            int bits = buffer.getInt(i);

            sum += (bits & 0xff000000) >> 24;
            sum += (bits & 0x00ff0000) >> 16;
            sum += (bits & 0x0000ff00) >> 8;
            sum += (bits & 0x000000ff);
        }

        return sum;
    }

    @Benchmark
    public long getLong() {
        sum = 0;

        for (int i = 0; i < SIZE; i += 8) {
            long bits = buffer.getLong(i);

            sum += (bits & 0xff00000000000000l) >> 56;
            sum += (bits & 0x00ff000000000000l) >> 48;
            sum += (bits & 0x0000ff0000000000l) >> 40;
            sum += (bits & 0x000000ff00000000l) >> 32;
            sum += (bits & 0x00000000ff000000l) >> 24;
            sum += (bits & 0x0000000000ff0000l) >> 16;
            sum += (bits & 0x000000000000ff00l) >> 8;
            sum += (bits & 0x00000000000000ffl);
        }

        return sum;
    }

    @Benchmark
    public long forEachByte() {
        sum = 0;

        buffer.forEachByte(calculator);

        return sum;
    }

    private class SumCalculator implements ByteProcessor {

        @Override
        public boolean process(final byte value) {
            sum += value;

            return true;
        }

    }

}
