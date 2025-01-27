# Tagline Handbook

Tagline is a Financial Information Exchange (FIX) codec for Netty. This
handbook assumes that you have prior experience working with both FIX and
Netty.

## Data Types

Tagline implements the following standard data types:

  - **Boolean** corresponds to the primitive type `boolean`.
  - **Char** corresponds to the primitive type `char`.
  - **Int** corresponds to the primitive type `long`.
  - **Float** is implemented in terms of the custom interface `FixFloat` and
    the primitive type `double`.
  - **String** is implemented in terms of the standard interface
    `CharSequence`.
  - **UTCDate** and **LocalMktDate** are implemented in terms of the custom
    interface `FixDate`.
  - **UTCTimeOnly** is implemented in terms of the custom interface `FixTime`.
  - **UTCTimestamp** is implemented in terms of the custom interface
    `FixTimestamp` and the primitive type `long`.

The custom `FixFloat` interface represents a decimal value in terms of an
unscaled `long` value and an `int` scale, the latter specifying the number of
digits to the right of the decimal point. It is mutable so that you can reuse
instances of it and thus reduce your application's memory allocation rate. As
Tagline can also decode Float values directly as `double` and encode them
directly from `double`, you might not need to deal with this interface at all.

Tagline decodes and encodes Char and String values using ISO-8859-1. When
encountering a Unicode character that cannot be represented in ISO-8859-1,
Tagline encodes it as `'?'` like `StandardCharsets.ISO_8859_1` does.

The custom interfaces for dates and times are mutable as well, unlike the
`java.time` classes. Note that Tagline can decode UTCTimestamp values directly
as `long` and encode them directly from `long` so you might not have to work
with the `FixTimestamp` interface either.

If the built-in data types are not suitable, you can always access the raw
bytes of a value directly.

## Messages

Tagline uses three representations of a message.

An `OutboundFixMessage` represents an outgoing message. It wraps a `ByteBuf`
and implements a set of methods to append fields of the standard data types
into the message. When you append a field, `OutboundFixMessage` immediately
encodes it into the wrapped `ByteBuf`.

An `InboundFixMessage` represents an incoming message, and it, too, wraps a
`ByteBuf`. It handles the BeginString(8), BodyLength(9), and CheckSum(10)
fields and contains the incoming message in the wrapped `ByteBuf`.

`FixFieldList` is a data structure offering efficient read-only random access
to a sequence of fields. Mirroring `OutboundFixMessage`, it implements a set
of methods to access fields as the standard data types. To achieve this,
`FixFieldList` decodes the values of accessed fields lazily on demand. Tagline
uses `FixFieldList` to represent a received message.

## Limits

Tagline has the following limits:

  - A tag in an outgoing message must be at least 0 and can be at most 9999999.
    Attempting to add a field with a tag that is outside these limits to an
    outgoing message throws an `IllegalArgumentException`.
  - A tag in an incoming message must be at least 1 and can be at most
    `Integer.MAX_VALUE`. Iterating over a field in a received message with a
    tag that is outside these limits throws a `FixDecoderException`.
  - The BodyLength(9) value of an outgoing message can be at most 9999999 bytes
    (roughly 9.5 MiB). Attempting to send a message whose BodyLength(9) exceeds
    this limit throws an `IllegalArgumentException`.
  - By default, the BodyLength(9) value of an incoming message can be at most
    `Integer.MAX_VALUE` bytes (roughly 2 GiB), but you can lower this limit.
    Attempting to receive a message whose BodyLength(9) exceeds this limit
    throws a `TooLongFixMessageException`.
  - The Int data type has the minimum value of `Long.MIN_VALUE` and the maximum
    value of `Long.MAX_VALUE`. Attempting to read a value that is outside of
    these limits as an Int throws a `FixDecoderException`.
  - The Float data type has the minimum unscaled value of `Long.MIN_VALUE` and
    the maximum unscaled value of `Long.MAX_VALUE` as well as the minimum scale
    of 0 and the maximum scale of 18. In other words, a Float can consist of at
    most 19 digits, out of which 18 can be to the right of the decimal point.
    Attempting read a value that is outside of these limits as a Float throws a
    `FixDecoderException`.

## Usage

See below for common Tagline use cases.

### Configure a channel

Tagline represents an outgoing message as `OutboundFixMessage` and an incoming
message first as `InboundFixMessage` and then as `FixFieldList`. Add an
`OutboundFixMessageEncoder` to a channel to encode outgoing messages and an
`InboundFixMessageDecoder` and a `FixFieldListDecoder` to decode incoming
messages. Note that all handlers are stateful so each channel needs their own
instances.

Configure a channel:
```java
var pipeline = channel.pipeline();

pipeline.addLast(new OutboundFixMessageEncoder(), new InboundFixMessageDecoder(),
    new FixFieldListDecoder());
```

### Send a message

`OutboundFixMessage` provides methods for appending fields of the standard data
types. Tagline will only populate the BeginString(8), BodyLength(9), and
CheckSum(10) fields; you have to handle the rest. This includes the header
fields, such as MsgType(35), as well as those in the message body.

Send an Order Single message using FIX 4.2:
```java
var currentTimeMillis = System.currentTimeMillis();

channel.writeAndFlush(new DefaultOutboundFixMessage(FixVersion.FIX_4_2, channel.alloc())
    .addChar(35, 'D')
    .addString(49, "initiator")
    .addString(56, "acceptor")
    .addInt(34, 2)
    .addTimestamp(52, currentTimeMillis, FixTimeFormat.WITH_MILLISECONDS)
    .addString(11, "1")
    .addChar(21, '1')
    .addString(55, "FOO")
    .addChar(54, '1')
    .addTimestamp(60, currentTimeMillis, FixTimeFormat.WITH_MILLISECONDS)
    .addInt(38, 100)
    .addChar(40, '2')
    .addFloat(44, 150.00, 2));
```

### Handle a received message

The `FixValue` interface represents a value in a received message. It extends
the standard `CharSequence` interface for treating the value as a String, and
it also contains methods to decode the value as any of the other standard data
types.

Print out the fields in a received message:
```java
class Handler extends SimpleChannelInboundHandler<FixFieldList> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FixFieldList msg) {
        for (int i = 0; i < msg.size(); i++)
            System.out.printf("%s=%s\n", msg.tagAt(i), msg.valueAt(i));
    }

}
```

Note that the `FixFieldList` instances produced by `FixFieldListDecoder` use
reference counting and resource pooling to reduce your application's memory
allocation rate. A `SimpleChannelInboundHandler`, as above, automatically
decrements the reference count. If you don't want that behavior, you can manage
the reference count manually using `ReferenceCountUtil#retain()` and
`ReferenceCountUtil#release()`.

### Handle a garbled message

A received message that does not follow the correct format is marked as
garbled. Applications should generally ignore garbled messages.

`FixFieldListDecoder` throws a `GarbledFixMessageException` on a garbled
message.

### Check the BeginString(8) value

By default, Tagline does not check the BeginString(8) value in a received
message. Configure `FixFieldListDecoder` to enable this check.

Check that the BeginString(8) value corresponds to FIX 4.2:
```java
var config = FixFieldListDecoderConfig.newBuilder()
    .setVersion(FixVersion.FIX_4_2)
    .build();

var pipeline = channel.pipeline();

pipeline.addLast(new OutboundFixMessageEncoder(), new InboundFixMessageDecoder(),
    new FixFieldListDecoder(config));
```

If the BeginString(8) check is enabled and the BeginString(8) value in a
message does not match the configuration, `FixFieldListDecoder` considers the
message garbled and throws a `GarbledFixMessageException`.

### Limit the BodyLength(9) value

By default, Tagline accepts a BodyLength(9) value up to `Integer.MAX_VALUE`
bytes (roughly 2 GiB) in incoming messages. Configure
`InboundFixMessageDecoder` to lower this limit.

Limit the BodyLength(9) value to 64 KiB:
```java
var config = InboundFixMessageDecoderConfig.newBuilder()
    .setMaxBodyLength(65536)
    .build();

var pipeline = channel.pipeline();

pipeline.addLast(new OutboundFixMessageEncoder(), new InboundFixMessageDecoder(config),
    new FixFieldListDecoder());
```

If a received BodyLength(9) value exceeds the limit, `InboundFixMessageDecoder`
throws a `TooLongFixMessageException`. Message reception is effectively lost at
this point, and you should close the channel either immediately or right after
sending a message indicating the error.

Close the channel when receiving a BodyLength(9) value that exceeds the limit:
```java
class Handler extends SimpleChannelInboundHandler<FixFieldList> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof TooLongFixMessageException) {
            ctx.close();

            return;
        }

        super.exceptionCaught(ctx, cause);
    }

}
```

### Check the CheckSum(10) value

By default, Tagline does not check that the CheckSum(10) value in a received
message matches the message content. Configure `FixFieldListDecoder` to enable
this check.

Enable the CheckSum(10) check:
```java
var config = FixFieldListDecoderConfig.newBuilder()
    .setCheckSumEnabled(true)
    .build();

var pipeline = channel.pipeline();

pipeline.addLast(new OutboundFixMessageEncoder(), new InboundFixMessageDecoder(),
    new FixFieldListDecoder(config));
```

If the CheckSum(10) check is enabled and the CheckSum(10) value in a message
does not match the message content, `FixFieldListDecoder` considers the message
garbled and throws a `GarbledFixMessageException`.
