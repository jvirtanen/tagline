/*
 * Copyright 2025 Jussi Virtanen
 */
package org.jvirtanen.tagline.acceptor;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
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
        var eventLoopGroup = createEventLoopGroup(transport, 1);
        var serverChannelFactory = createServerChannelFactory(transport);

        new Tester(eventLoopGroup.next(), serverChannelFactory, port);
    }

    private static EventLoopGroup createEventLoopGroup(final String transport, final int numThreads) {
        switch (transport) {
        case "epoll":
            return new EpollEventLoopGroup(numThreads);
        case "kqueue":
            return new KQueueEventLoopGroup(numThreads);
        default:
            return new NioEventLoopGroup(numThreads);
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
