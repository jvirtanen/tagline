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

import io.netty.channel.ChannelFactory;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueIoHandler;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.HdrHistogram.Histogram;

class Initiator {

    private static final long NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1);

    private static final Locale LOCALE = Locale.US;

    public static void main(final String[] args) {
        if (args.length != 5)
            usage();

        try {
            var transport = args[0];
            var host = args[1];
            int port = Integer.parseInt(args[2]);
            int messageRate = Integer.parseInt(args[3]);
            int messageCount = Integer.parseInt(args[4]);

            main(transport, new InetSocketAddress(host, port), messageRate, messageCount);
        } catch (IllegalArgumentException e) {
            usage();
        }
    }

    private static void main(final String transport, final InetSocketAddress address, final int messageRate, final int messageCount) {
        var ioHandlerFactory = createIoHandlerFactory(transport);
        var eventLoopGroup = new MultiThreadIoEventLoopGroup(1, ioHandlerFactory);
        var channelFactory = createChannelFactory(transport);
        var histogram = new Histogram(3);

        long intervalNanos = NANOS_PER_SECOND / messageRate;

        var tester = new Tester(eventLoopGroup.next(), channelFactory, address, histogram);

        printf("Warming up...\n");

        tester.communicate(messageCount, intervalNanos).awaitUninterruptibly();

        histogram.reset();

        printf("Benchmarking...\n");

        tester.communicate(messageCount, intervalNanos).awaitUninterruptibly();

        eventLoopGroup.shutdownGracefully();

        printf("Results (n = %d)\n", messageCount);
        printf("\n");
        printf("   50.00%%: %10.2f µs\n", histogram.getValueAtPercentile(50.0) / 1000.0);
        printf("   90.00%%: %10.2f µs\n", histogram.getValueAtPercentile(90.0) / 1000.0);
        printf("   99.00%%: %10.2f µs\n", histogram.getValueAtPercentile(99.0) / 1000.0);
        printf("   99.90%%: %10.2f µs\n", histogram.getValueAtPercentile(99.9) / 1000.0);
        printf("   99.99%%: %10.2f µs\n", histogram.getValueAtPercentile(99.99) / 1000.0);
        printf("  100.00%%: %10.2f µs\n", histogram.getValueAtPercentile(100.0) / 1000.0);
        printf("\n");
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

    private static ChannelFactory<SocketChannel> createChannelFactory(final String transport) {
        switch (transport) {
        case "epoll":
            return EpollSocketChannel::new;
        case "kqueue":
            return KQueueSocketChannel::new;
        default:
            return NioSocketChannel::new;
        }
    }

    private static void usage() {
        System.err.println("Usage: epoll|kqueue|nio <host> <port> <message-rate> <message-count>");
        System.exit(2);
    }

    private static void printf(String format, Object... args) {
        System.out.printf(LOCALE, format, args);
    }

}
