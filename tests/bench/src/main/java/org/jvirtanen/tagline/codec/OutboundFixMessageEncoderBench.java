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

    @Setup(Level.Trial)
    public void setUp() {
        channel = new EmbeddedChannel(new OutboundFixMessageEncoder());
    }

    @TearDown(Level.Trial)
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
