/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class FixDateDecoderBench extends Bench {

    private byte[] bytes;

    private DefaultFixTimestamp timestamp;;

    @Setup(Level.Iteration)
    public void setUp() {
        bytes = new byte[] { '2', '0', '2', '4', '0', '5', '2', '6', };

        timestamp = new DefaultFixTimestamp();
    }

    @Benchmark
    public DefaultFixTimestamp decode() {
        FixDateDecoder.decode(bytes, 8, timestamp);

        return timestamp;
    }

}
