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

import io.netty.channel.ChannelFactory;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueueIoHandler;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

class Acceptor {

    public static void main(final String[] args) {
        if (args.length != 2)
            usage();

        try {
            var transport = args[0];
            int port = Integer.parseInt(args[1]);

            main(transport, port);
        } catch (IllegalArgumentException e) {
            usage();
        }
    }

    private static void main(final String transport, final int port) {
        var ioHandlerFactory = createIoHandlerFactory(transport);
        var eventLoopGroup = new MultiThreadIoEventLoopGroup(1, ioHandlerFactory);
        var serverChannelFactory = createServerChannelFactory(transport);

        new Tester(eventLoopGroup.next(), serverChannelFactory, port);
    }

    private static IoHandlerFactory createIoHandlerFactory(final String transport) {
        switch (transport) {
        case "epoll":
            return EpollIoHandler.newFactory();
        case "kqueue":
            return KQueueIoHandler.newFactory();
        default:
            return NioIoHandler.newFactory();
        }
    }

    private static ChannelFactory<ServerSocketChannel> createServerChannelFactory(final String transport) {
        switch (transport) {
        case "epoll":
            return EpollServerSocketChannel::new;
        case "kqueue":
            return KQueueServerSocketChannel::new;
        default:
            return NioServerSocketChannel::new;
        }
    }

    private static void usage() {
        System.err.println("Usage: epoll|kqueue|nio <port>");
        System.exit(2);
    }

}
