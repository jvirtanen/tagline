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
