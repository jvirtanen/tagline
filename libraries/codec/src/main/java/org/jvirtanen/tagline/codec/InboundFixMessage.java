/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * <p>An inbound FIX message.</p>
 *
 * <p>The {@linkplain #content() content} of an inbound FIX message contains
 * all the bytes in the received message.</p>
 *
 * <p>An inbound FIX message that does not follow the correct format is marked
 * as garbled. Applications should generally ignore garbled messages.</p>
 *
 * <p>A garbled message that could not be decoded correctly does not have the
 * {@linkplain #version() BeginString(8)}, {@linkplain #bodyLength()
 * BodyLength(9)}, and {@linkplain #checkSum() CheckSum(10)} values. However,
 * you can always access all the bytes of a garbled message with
 * {@link #content()}.</p>
 *
 * <p>{@link InboundFixMessageDecoder} decodes the CheckSum(10) field but does
 * not check it against the message content. Use {@link FixCheckSumCalculator}
 * to do that:</p>
 * <pre>
 * FixCheckSumCalculator checkSum = new FixCheckSumCalculator();
 *
 * if (message.checkSum() != checkSum.calculate(message))
 *     message.setGarbled();
 * </pre>
 */
public interface InboundFixMessage extends FixMessage {

    /**
     * Get the protocol version.
     *
     * @return the protocol version or {@code null} if this message is garbled
     *     and could not be decoded correctly
     */
    @Override
    FixVersion version();

    /**
     * Returns true if this message is garbled, otherwise returns false.
     *
     * @return true if this message is garbled, otherwise false
     */
    boolean isGarbled();

    /**
     * Mark this message as garbled.
     *
     * @return this instance
     */
    InboundFixMessage setGarbled();

    /**
     * Get the body offset.
     *
     * @return the body offset or {@code -1} if this message is garbled and
     *     could not be decoded correctly
     */
    int bodyOffset();

    /**
     * Get the BodyLength(9) value.
     *
     * @return the BodyLength(9) value or {@code -1} if this message is garbled
     *     and could not be decoded correctly
     */
    int bodyLength();

    /**
     * Get the CheckSum(10) value.
     *
     * @return the CheckSum(10) value or {@code -1} if this message is garbled
     *     and could not be decoded correctly
     */
    int checkSum();

}
