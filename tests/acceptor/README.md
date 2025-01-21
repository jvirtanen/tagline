# Tagline Acceptor

Tagline Acceptor is a test application for Tagline Codec. It responds to
Order Single messages with Execution Report messages.

## Development

Build Tagline Acceptor:
```
mvn package -am -f ../.. -pl tests/acceptor
```

## Usage

Run Tagline Acceptor:
```
java -jar tagline-acceptor.jar epoll|kqueue|nio <port>
```
