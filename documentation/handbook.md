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

FIX generally supports only the ASCII character set for Char and String values.
However, Netty provides an efficient way to encode into the ISO-8859-1
character set, which is a superset of ASCII and a subset of Unicode. Because of
this, Tagline decodes and encodes Char and String values using ISO-8859-1. When
encountering a Unicode character that cannot be represented in ISO-8859-1,
Tagline encodes it as `'?'` like `StandardCharsets.ISO_8859_1` does.

The custom interfaces for dates and times are mutable as well, unlike the
`java.time` classes. Note that Tagline can decode UTCTimestamp values directly
as `long` and encode them directly from `long` so you might not have to work
with the `FixTimestamp` interface either.

If the built-in data types are not suitable, you can always access the raw
bytes of a value directly.

## Usage

See below for common Tagline use cases.

### Configure a channel

Tagline represents an incoming message as `InboundFixMessage` and an outgoing
message as `OutboundFixMessage`. Add an `InboundFixMessageDecoder` to a channel
to decode incoming messages and an `OutboundFixMessageEncoder` to encode
outgoing messages. Note that both handlers are stateful so each channel needs
their own instances.

Configure a channel:
```java
var pipeline = channel.pipeline();

pipeline.addLast(new InboundFixMessageDecoder(), new OutboundFixMessageEncoder());
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

When you get a hold of an `InboundFixMessage` instance, Tagline has only
attempted to parse the BeginString(8), BodyLength(9), and CheckSum(10) fields.
Handling the other fields is up to you, as is checking the BeginString(8) and
CheckSum(10) values.

The `FixFieldIterator` class provides sequential access to the fields in a
received message. It implements the standard `Iterator` interface but is
reusable: invoke `FixFieldIterator#iterate()` for each received message. Also
note that each invocation of `FixFieldIterator#next()` invalidates the
previously returned value.

The `FixField` interface represents a field in a received message. It extends
the standard `CharSequence` interface for treating the value as a String, and
it also contains methods to decode the value as any of the other standard data
types.

Print out the fields in a received message:
```java
class Handler extends SimpleChannelInboundHandler<InboundFixMessage> {

    private final FixFieldIterator fields = new FixFieldIterator();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, InboundFixMessage msg) {
        if (msg.isGarbled())
            return;

        for (var field : fields.iterate(msg))
            System.out.printf("%s=%s\n", field.tag(), field.asString());
    }

}
```

### Handle a garbled message

A received message that does not follow the correct format regarding the
BeginString(8), BodyLength(9), and CheckSum(10) fields is marked as garbled.
Applications should generally ignored garbled messages. However, you can still
access all the bytes of a garbled message with `InboundFixMessage#content()`.

### Check the BeginString(8) value

Although Tagline parses the BeginString(8) field in received messages, it's
up to you to check that it matches what you expect. Tagline represents the
BeginString(8) value as `FixVersion`, and you can access it for a received
message as `InboundFixMessage#version()`.

Check that the BeginString(8) value corresponds to FIX 4.2:
```java
if (!FixVersion.FIX_4_2.equals(message.version()))
    throw new IllegalStateException("Invalid BeginString(8) value");
```

### Check the CheckSum(10) value

Tagline parses the CheckSum(10) field in a received message but does not
automatically check that it matches the message content. To do so, access the
value as `InboundFixMessage#checkSum()` and use `FixCheckSumCalculator` to
calculate the checksum for the message content to compare against.

Check that the CheckSum(10) value matches the message content:
```java
var calculator = new FixCheckSumCalculator();

if (message.checkSum() != calculator.calculate(message))
    throw new IllegalStateException("Invalid CheckSum(10) value");
```
