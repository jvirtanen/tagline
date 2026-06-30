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

import static java.nio.charset.StandardCharsets.*;

import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixIntDecoderBench extends Bench {

    private byte[] zeroBytes;
    private byte[] maxFastPathBytes;
    private byte[] maxValueBytes;

    @Setup(Level.Trial)
    public void setUp() {
        zeroBytes = getBytes("0");
        maxFastPathBytes = getBytes("999999999999999999");
        maxValueBytes = getBytes(Long.toString(Long.MAX_VALUE));
    }

    @Benchmark
    public long decodeZero() {
        return decode(zeroBytes);
    }

    @Benchmark
    public long decodeMaxFastPath() {
        return decode(maxFastPathBytes);
    }

    @Benchmark
    public long decodeMaxValue() {
        return decode(maxValueBytes);
    }

    private long decode(final byte[] bytes) {
        return FixIntDecoder.decode(bytes, 0, bytes.length);
    }

    private static final byte[] getBytes(final String value) {
        return value.getBytes(ISO_8859_1);
    }

}
