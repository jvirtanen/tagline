/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.jvirtanen.tagline.codec.FixTimeFormat.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jvirtanen.tagline.bench.Bench;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

public class DefaultOutboundFixMessageBench extends Bench {

    private static final int CL_ORD_ID = 11;
    private static final int HANDL_INST = 21;
    private static final int MSG_SEQ_NUM = 34;
    private static final int MSG_TYPE = 35;
    private static final int ORDER_QTY = 38;
    private static final int ORD_TYPE = 40;
    private static final int PRICE = 44;
    private static final int SENDER_COMP_ID = 49;
    private static final int SENDING_TIME = 52;
    private static final int SIDE = 54;
    private static final int SYMBOL = 55;
    private static final int TARGET_COMP_ID = 56;
    private static final int TRANSACT_TIME = 60;

    private static final char AUTOMATED_EXECUTION_NO_INTERVENTION = '1';
    private static final char BUY = '1';
    private static final char LIMIT = '2';
    private static final char ORDER_SINGLE = 'D';

    private ByteBuf content;
    private DefaultOutboundFixMessage message;

    private long currentTimeMillis;

    @Setup(Level.Iteration)
    public void setUp() {
        content = Unpooled.directBuffer(1024);
        message = new DefaultOutboundFixMessage(FIX_4_2, content);

        currentTimeMillis = System.currentTimeMillis();
    }

    @Benchmark
    public DefaultOutboundFixMessage addInt() {
        content.clear();

        return message.addInt(MSG_SEQ_NUM, 5);
    }

    @Benchmark
    public DefaultOutboundFixMessage addFloat() {
        content.clear();

        return message.addFloat(PRICE, 150.00, 2);
    }

    @Benchmark
    public DefaultOutboundFixMessage addString() {
        content.clear();

        return message.addString(SENDER_COMP_ID, "initiator");
    }

    @Benchmark
    public DefaultOutboundFixMessage addTimestamp() {
        content.clear();

        return message.addTimestamp(SENDING_TIME, currentTimeMillis, WITH_MILLISECONDS);
    }

    @Benchmark
    public DefaultOutboundFixMessage addMany() {
        content.clear();

        message.addChar(MSG_TYPE, ORDER_SINGLE);
        message.addString(SENDER_COMP_ID, "initiator");
        message.addString(TARGET_COMP_ID, "acceptor");
        message.addInt(MSG_SEQ_NUM, 5);
        message.addTimestamp(SENDING_TIME, currentTimeMillis, WITH_MILLISECONDS);
        message.addString(CL_ORD_ID, "123");
        message.addChar(HANDL_INST, AUTOMATED_EXECUTION_NO_INTERVENTION);
        message.addString(SYMBOL, "FOO");
        message.addChar(SIDE, BUY);
        message.addTimestamp(TRANSACT_TIME, currentTimeMillis, WITH_MILLISECONDS);
        message.addInt(ORDER_QTY, 100);
        message.addChar(ORD_TYPE, LIMIT);
        message.addFloat(PRICE, 150.00, 2);

        return message;
    }

}
