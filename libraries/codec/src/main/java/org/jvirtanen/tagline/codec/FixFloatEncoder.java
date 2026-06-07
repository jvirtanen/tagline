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

class FixFloatEncoder {

    static final int MAX_LENGTH = 23;

    private final byte[] bytes;

    FixFloatEncoder() {
        bytes = new byte[MAX_LENGTH];

        bytes[MAX_LENGTH - 1] = SOH;
    }

    void encode(final FixFloat value, final ByteBuf buffer) {
        if (value.unscaledValue() < 0)
            encodeNegative(value, buffer);
        else
            encodePositive(value, buffer);
    }

    private void encodePositive(final FixFloat value, final ByteBuf buffer) {
        int i = MAX_LENGTH - 1;

        long unscaledValue = value.unscaledValue();
        int scale = value.scale();

        while (scale-- > 0) {
            bytes[--i] = (byte)('0' + unscaledValue % 10);

            unscaledValue /= 10;
        }

        if (value.scale() > 0)
            bytes[--i] = '.';

        do {
            bytes[--i] = (byte)('0' + unscaledValue % 10);

            unscaledValue /= 10;
        } while (unscaledValue > 0);

        buffer.writeBytes(bytes, i, MAX_LENGTH - i);
    }

    private void encodeNegative(final FixFloat value, final ByteBuf buffer) {
        int i = MAX_LENGTH - 1;

        long unscaledValue = value.unscaledValue();
        int scale = value.scale();

        while (scale-- > 0) {
            bytes[--i] = (byte)('0' - unscaledValue % 10);

            unscaledValue /= 10;
        }

        if (value.scale() > 0)
            bytes[--i] = '.';

        do {
            bytes[--i] = (byte)('0' - unscaledValue % 10);

            unscaledValue /= 10;
        } while (unscaledValue < 0);

        bytes[--i] = '-';

        buffer.writeBytes(bytes, i, MAX_LENGTH - i);
    }

}
