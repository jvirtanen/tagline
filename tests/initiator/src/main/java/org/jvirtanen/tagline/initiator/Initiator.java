/**
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.initiator;

import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
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
        var eventLoopGroup = createEventLoopGroup(transport, 1);
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
