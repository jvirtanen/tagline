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
        return calculate(message.content(), 0, message.bodyOffset() + message.bodyLength());
    }

    /**
     * Calculate the CheckSum(10) value over the readable bytes in a buffer.
     *
     * @param buffer a buffer
     * @return the CheckSum(10) value
     */
    public int calculate(final ByteBuf buffer) {
        int readerIndex = buffer.readerIndex();

        return calculate(buffer, readerIndex, readerIndex + buffer.readableBytes());
    }

    /**
     * Calculate the CheckSum(10) value over a span of bytes in a buffer.
     *
     * @param buffer a buffer
     * @param index the starting index of the span
     * @param length the length of the span
     * @return the CheckSum(10) value
     */
    public int calculate(final ByteBuf buffer, final int index, final int length) {
        checkSum = 0;

        buffer.forEachByte(index, length, processor);

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
