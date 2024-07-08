/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;

/**
 * <p>An inbound FIX message.</p>
 *
 * <p>An inbound FIX message that does not follow the correct format regarding
 * the BeginString(8), BodyLength(9), and CheckSum(10) fields is marked as
 * garbled. Applications should generally ignore garbled messages.</p>
 *
 * <p>The {@linkplain #content() content} of a non-garbled inbound FIX message
 * contains all the bytes in the message up to and excluding the CheckSum(10)
 * field. {@link InboundFixMessageDecoder} decodes the CheckSum(10) field but
 * does not check it against the message. Use {@link FixCheckSumCalculator} to
 * do that:</p>
 * <pre>
 * FixCheckSumCalculator calculator = new FixCheckSumCalculator();
 *
 * if (message.checkSum() != calculator.calculate(message))
 *     throw new IllegalStateException("Invalid CheckSum(10) value");
 * </pre>
 */
public interface InboundFixMessage extends FixMessage {

    /**
     * Get the content. For a non-garbled message, the content contains all the
     * bytes in the message up to and excluding the CheckSum(10) field. For a
     * garbled message, the content contains all the bytes in the message.
     *
     * @return the content
     */
    @Override
    ByteBuf content();

    /**
     * Get the protocol version.
     *
     * @return the protocol version or {@code null} if the message is garbled
     */
    @Override
    FixVersion version();

    /**
     * Returns true if the message is garbled, otherwise returns false.
     *
     * @return true if the message is garbled, otherwise false
     */
    boolean isGarbled();

    /**
     * Get the body offset.
     *
     * @return the body offset or {@code -1} if the message is garbled
     */
    int bodyOffset();

    /**
     * Get the CheckSum(10) value.
     *
     * @return the CheckSum(10) value or {@code -1} if the message is garbled
     */
    int checkSum();

}
