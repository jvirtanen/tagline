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
package org.jvirtanen.tagline.initiator;

import static org.jvirtanen.tagline.codec.FixTimeFormat.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.HdrHistogram.Histogram;
import org.jvirtanen.tagline.codec.DefaultOutboundFixMessage;
import org.jvirtanen.tagline.codec.FixFieldList;
import org.jvirtanen.tagline.codec.FixFieldListDecoder;
import org.jvirtanen.tagline.codec.InboundFixMessageDecoder;
import org.jvirtanen.tagline.codec.OutboundFixMessage;
import org.jvirtanen.tagline.codec.OutboundFixMessageEncoder;

class Tester {

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
    private static final char EXECUTION_REPORT = '8';
    private static final char LIMIT = '2';
    private static final char ORDER_SINGLE = 'D';

    private static final ByteBufAllocator ALLOCATOR = ByteBufAllocator.DEFAULT;

    private final EventLoop eventLoop;

    private final Histogram histogram;

    private final Channel channel;

    private long msgSeqNum;

    private ScheduledFuture<?> future;

    Tester(final EventLoop eventLoop, final ChannelFactory<SocketChannel> channelFactory, final InetSocketAddress address, final Histogram histogram) {
        this.eventLoop = eventLoop;

        this.histogram = histogram;

        var bootstrap = new Bootstrap()
            .group(eventLoop)
            .channelFactory(channelFactory)
            .option(ChannelOption.ALLOCATOR, ALLOCATOR)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(final SocketChannel channel) {
                    var pipeline = channel.pipeline();

                    pipeline.addLast(
                            new OutboundFixMessageEncoder(),
                            new InboundFixMessageDecoder(),
                            new FixFieldListDecoder(),
                            new Handler()
                        );
                }
            });

        this.channel = bootstrap.connect(address).awaitUninterruptibly().channel();

        this.msgSeqNum = 1;
    }

    Future<Void> communicate(final int messageCount, final long intervalNanos) {
        long currentTimeMillis = System.currentTimeMillis();

        long initialMsgSeqNum = msgSeqNum;

        Promise<Void> promise = eventLoop.newPromise();

        future = eventLoop.scheduleAtFixedRate(() -> {
            if (histogram.getTotalCount() >= messageCount) {
                future.cancel(false);
                promise.trySuccess(null);

                return;
            }

            if (msgSeqNum - initialMsgSeqNum >= messageCount)
                return;

            long clOrdId = System.nanoTime();

            var message = createMessage(msgSeqNum++, currentTimeMillis, clOrdId);

            channel.writeAndFlush(message);
        }, 0, intervalNanos, TimeUnit.NANOSECONDS);

        return promise;
    }

    private class Handler extends SimpleChannelInboundHandler<FixFieldList> {

        @Override
        public void channelRead0(final ChannelHandlerContext ctx, final FixFieldList msg) {
            long receiveNanos = System.nanoTime();

            for (int i = 0; i < msg.size(); i++) {
                int tag = msg.tagAt(i);
                var value = msg.valueAt(i);

                if (tag == MSG_TYPE && value.asChar() != EXECUTION_REPORT)
                    return;

                if (tag != CL_ORD_ID)
                    continue;

                long sendNanos = value.asInt();

                histogram.recordValue(receiveNanos - sendNanos);

                break;
            }
        }

    }

    private static OutboundFixMessage createMessage(final long msgSeqNum, final long currentTimeMillis, final long clOrdId) {
        return new DefaultOutboundFixMessage(FIX_4_2, ALLOCATOR)
            .addChar(MSG_TYPE, ORDER_SINGLE)
            .addString(SENDER_COMP_ID, "initiator")
            .addString(TARGET_COMP_ID, "acceptor")
            .addInt(MSG_SEQ_NUM, msgSeqNum)
            .addTimestamp(SENDING_TIME, currentTimeMillis, WITH_MILLISECONDS)
            .addInt(CL_ORD_ID, clOrdId)
            .addChar(HANDL_INST, AUTOMATED_EXECUTION_NO_INTERVENTION)
            .addString(SYMBOL, "FOO")
            .addChar(SIDE, BUY)
            .addTimestamp(TRANSACT_TIME, currentTimeMillis, WITH_MILLISECONDS)
            .addInt(ORDER_QTY, 100)
            .addChar(ORD_TYPE, LIMIT)
            .addFloat(PRICE, 150.00, 2);
    }

}
