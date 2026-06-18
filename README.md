# Tagline

Tagline is a fast Financial Information Exchange (FIX) codec for Netty. It's
not a full-blown FIX protocol implementation, but you can use it to build one.
If you have used Netty's built-in codecs, such as HTTP, you shouldn't have
trouble using Tagline.

Tagline decodes incoming FIX messages and encodes outgoing FIX messages.
It handles the BeginString(8), BodyLength(9), and CheckSum(10) fields and
implements the standard FIX data types. Tagline is for you if you're building
a low-latency FIX application or a higher-level FIX protocol implementation.

Tagline requires Java 11 or newer and Netty 4.2.x.

## Installation

Add Tagline to your project:
```xml
<dependency>
  <groupId>org.jvirtanen.tagline</groupId>
  <artifactId>tagline-codec</artifactId>
  <version><!-- the latest version --></version>
</dependency>
```

See [the release notes](CHANGELOG.md) for the latest version.

## Usage

Add Tagline to a channel:
```java
var pipeline = channel.pipeline();

pipeline.addLast(new OutboundFixMessageEncoder(), new InboundFixMessageDecoder(),
    new FixFieldListDecoder());
```

Send a FIX message:
```java
channel.writeAndFlush(new DefaultOutboundFixMessage(FixVersion.FIX_4_2, channel.alloc())
    .addChar(35, 'A')
    .addString(49, "initiator")
    .addString(56, "acceptor")
    .addInt(34, 1)
    .addTimestamp(52, System.currentTimeMillis(), FixTimeFormat.WITH_MILLISECONDS)
    .addInt(98, 0)
    .addInt(108, 30));
```

See [more use cases](documentation/handbook.md#usage) in Tagline Handbook.

## Performance

Tagline incurs as little performance overhead as possible. To illustrate, it
encodes an Order Single (D) message in 135 ns and decodes one in 70 ns on an
Apple MacBook Pro (M1 Pro, 2021) with Eclipse Temurin 25 and macOS Tahoe.

Read [more about performance](documentation/handbook.md#performance) in Tagline
Handbook, or run [Tagline Bench](tests/bench), [Tagline
Acceptor](tests/acceptor), and [Tagline Initiator](tests/initiator) to see for
yourself.

## License

Copyright 2024 Jussi Virtanen.

Released under the Apache License, Version 2.0. See `LICENSE.txt` for details.
