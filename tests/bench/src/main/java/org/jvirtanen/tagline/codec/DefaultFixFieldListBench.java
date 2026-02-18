/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class DefaultFixFieldListBench extends Bench {

    private ByteBuf content;

    private FixFieldList fields;

    @Setup(Level.Iteration)
    public void setUp() {
        fields = new DefaultFixFieldList();

        content = buffer("35=D\u000149=initiator\u000156=acceptor\u0001"
                + "34=1\u000152=20240107-16:44:30.950\u000111=123\u0001"
                + "21=1\u000155=FOO\u000154=1\u000160=20240107-16:44:30.950\u0001"
                + "38=100\u000140=2\u000144=150.25\u0001");
    }

    @Benchmark
    public int decode() {
        fields.decode(content, content.readerIndex(), content.readableBytes());

        return fields.size();
    }

    private static ByteBuf buffer(final String value) {
        var buffer = Unpooled.directBuffer(value.length());

        buffer.writeBytes(value.getBytes(ISO_8859_1));

        return buffer;
    }

}
