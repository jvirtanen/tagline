/*
 * Copyright 2025 Jussi Virtanen
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
package org.jvirtanen.tagline.acceptor;

import static org.jvirtanen.tagline.codec.FixTimeFormat.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import org.jvirtanen.tagline.codec.DefaultOutboundFixMessage;
import org.jvirtanen.tagline.codec.FixFieldList;
import org.jvirtanen.tagline.codec.FixFieldListDecoder;
import org.jvirtanen.tagline.codec.InboundFixMessageDecoder;
import org.jvirtanen.tagline.codec.OutboundFixMessage;
import org.jvirtanen.tagline.codec.OutboundFixMessageEncoder;

class Tester {

    private static final int CL_ORD_ID = 11;
    private static final int CUM_QTY = 14;
    private static final int EXEC_ID = 17;
    private static final int EXEC_TRANS_TYPE = 20;
    private static final int EXEC_TYPE = 150;
    private static final int LEAVES_QTY = 151;
    private static final int MSG_SEQ_NUM = 34;
    private static final int MSG_TYPE = 35;
    private static final int ORDER_ID = 37;
    private static final int ORDER_QTY = 38;
    private static final int ORD_TYPE = 40;
    private static final int PRICE = 44;
    private static final int SENDER_COMP_ID = 49;
    private static final int SENDING_TIME = 52;
    private static final int SIDE = 54;
    private static final int SYMBOL = 55;
    private static final int TARGET_COMP_ID = 56;
    private static final int TRANSACT_TIME = 60;

    private static final char EXECUTION_REPORT = '8';
    private static final char NEW = '0';

    private static final ByteBufAllocator ALLOCATOR = ByteBufAllocator.DEFAULT;

    private long msgSeqNum;

    Tester(final EventLoop eventLoop, final ChannelFactory<ServerSocketChannel> serverChannelFactory, final int port) {
        var bootstrap = new ServerBootstrap()
            .group(eventLoop)
            .localAddress(port)
            .channelFactory(serverChannelFactory)
            .childOption(ChannelOption.ALLOCATOR, ALLOCATOR)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(final SocketChannel channel) {
                    var pipeline = channel.pipeline();

                    pipeline.addLast(
                            new OutboundFixMessageEncoder(),
                            new InboundFixMessageDecoder(),
                            new FixFieldListDecoder(),
                            new ChildHandler()
                        );
                }

            });

        bootstrap.bind().awaitUninterruptibly().channel();

        this.msgSeqNum = 1;
    }

    private class ChildHandler extends SimpleChannelInboundHandler<FixFieldList> {

        @Override
        public void channelRead0(final ChannelHandlerContext ctx, final FixFieldList msg) {
            ctx.writeAndFlush(createMessage(msgSeqNum++, msg));
        }

    }

    private static OutboundFixMessage createMessage(final long msgSeqNum, final FixFieldList msg) {
        var sendingTime = msg.valueOf(SENDING_TIME);
        var clOrdId = msg.valueOf(CL_ORD_ID);
        var symbol = msg.valueOf(SYMBOL);
        var side = msg.valueOf(SIDE);
        var orderQty = msg.valueOf(ORDER_QTY);
        var price = msg.valueOf(PRICE);

        return new DefaultOutboundFixMessage(FIX_4_2, ALLOCATOR)
            .addChar(MSG_TYPE, EXECUTION_REPORT)
            .addString(SENDER_COMP_ID, "acceptor")
            .addString(TARGET_COMP_ID, "initiator")
            .addInt(MSG_SEQ_NUM, msgSeqNum)
            .addString(SENDING_TIME, sendingTime)
            .addInt(ORDER_ID, msgSeqNum)
            .addString(CL_ORD_ID, clOrdId)
            .addInt(EXEC_ID, msgSeqNum)
            .addChar(EXEC_TRANS_TYPE, NEW)
            .addChar(EXEC_TYPE, NEW)
            .addString(SYMBOL, symbol)
            .addString(SIDE, side)
            .addString(ORDER_QTY, orderQty)
            .addString(PRICE, price)
            .addString(LEAVES_QTY, orderQty)
            .addInt(CUM_QTY, 0);
    }

}
