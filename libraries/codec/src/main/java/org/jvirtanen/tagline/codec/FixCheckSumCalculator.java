/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

/**
 * A FIX CheckSum(10) calculator.
 */
public class FixCheckSumCalculator {

    static final int MASK = 0xff;

    private final ByteProcessor processor;

    private int checkSum;

    /**
     * Construct a new instance.
     */
    public FixCheckSumCalculator() {
        processor = new Processor();

        checkSum = 0;
    }

    /**
     * Calculate the CheckSum(10) value.
     *
     * @param message a message
     * @return the CheckSum(10) value
     */
    public int calculate(final InboundFixMessage message) {
        return calculate(message.content());
    }

    /**
     * Calculate the CheckSum(10) value.
     *
     * @param buffer a buffer
     * @return the CheckSum(10) value
     */
    public int calculate(final ByteBuf buffer) {
        checkSum = 0;

        buffer.forEachByte(processor);

        return checkSum & MASK;
    }

    private class Processor implements ByteProcessor {

        @Override
        public boolean process(final byte value) {
            checkSum += value;

            return true;
        }

    }

}
