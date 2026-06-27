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

import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixTimeDecoderBench extends Bench {

    private byte[] bytes;

    private DefaultFixTimestamp timestamp;

    @Setup(Level.Iteration)
    public void setUp() {
        bytes = new byte[] { '2', '2', ':', '0', '5', ':', '3', '0', '.', '2', '5', '0', };

        timestamp = new DefaultFixTimestamp();
    }

    @Benchmark
    public DefaultFixTimestamp decodeSecond() {
        FixTimeDecoder.decode(bytes, 0, 8, timestamp);

        return timestamp;
    }

    @Benchmark
    public DefaultFixTimestamp decodeMilli() {
        FixTimeDecoder.decode(bytes, 0, 12, timestamp);

        return timestamp;
    }

}
