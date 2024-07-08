# Tagline Initiator

Tagline Initiator is a test application for Tagline Codec. It measures the
round-trip time (RTT) between sending an Order Single message and receiving
the corresponding Execution Report message.

## Development

Build Tagline Initiator:
```
mvn package -am -f ../.. -pl tests/initiator
```

## Usage

Run Tagline Initiator:
```
java -jar tagline-initiator.jar --epoll|--kqueue|--nio <host> <port> <message-rate> <message-count>
```

Specify the message rate as messages per second. To observe the best
performance, set the total message count high enough (e.g. 50k) so that the
JVM has an opportunity to optimize the program thoroughly.
