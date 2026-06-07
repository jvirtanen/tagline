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

import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

class FixBodyLengthDecoder implements ByteProcessor {

    private int bodyLength;

    int decode(final ByteBuf buffer, int index, int length) {
        bodyLength = 0;

        int sohIndex = buffer.forEachByte(index, length, this);
        if (sohIndex < 0)
            return sohIndex;

        if (sohIndex - index < 1)
            notBodyLength();

        return sohIndex + 1;
    }

    int bodyLength() {
        return bodyLength;
    }

    @Override
    public boolean process(final byte value) {
        if (value == SOH)
            return false;

        if (value < '0' || value > '9')
            notBodyLength();

        bodyLength = 10 * bodyLength + value - '0';
        if (bodyLength < 0)
            notBodyLength();

        return true;
    }

    private static void notBodyLength() throws FixDecoderException {
        throw new FixDecoderException("Invalid BodyLength(9) value");
    }

}
