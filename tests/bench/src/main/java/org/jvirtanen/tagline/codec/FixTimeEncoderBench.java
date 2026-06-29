/*
 * Copyright 2026 Jussi Virtanen
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

import static org.jvirtanen.tagline.codec.FixTimeFormat.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixTimeEncoderBench extends Bench {

    private ByteBuf buffer;

    private DefaultFixTimestamp timestamp;

    @Setup(Level.Iteration)
    public void setUp() {
        buffer = Unpooled.directBuffer(1024);

        timestamp = new DefaultFixTimestamp()
            .setHour(17)
            .setMinute(25)
            .setSecond(30)
            .setMilli(250);
    }

    @Benchmark
    public ByteBuf encodeSecond() {
        buffer.clear();

        FixTimeEncoder.encode(timestamp, WITH_SECONDS, buffer);

        return buffer;
    }

    @Benchmark
    public ByteBuf encodeMilli() {
        buffer.clear();

        FixTimeEncoder.encode(timestamp, WITH_MILLISECONDS, buffer);

        return buffer;
    }

}
