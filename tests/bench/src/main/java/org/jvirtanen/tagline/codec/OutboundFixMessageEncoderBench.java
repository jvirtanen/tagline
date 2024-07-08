/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

public class OutboundFixMessageEncoderBench extends Bench {

    private EmbeddedChannel channel;

    @Setup(Level.Iteration)
    public void setUp() {
        channel = new EmbeddedChannel(new OutboundFixMessageEncoder());
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        channel.finish();
    }

    @Benchmark
    public int empty() {
        var message = new DefaultOutboundFixMessage(FIX_4_2, channel.alloc().ioBuffer());

        channel.writeOutbound(message);

        int count = 0;

        while (true) {
            ByteBuf buffer = channel.readOutbound();
            if (buffer == null)
                break;

            buffer.release();

            count++;
        }

        return count;
    }

}
