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

class FixMediumEncoder {

    static final int MAX_LENGTH = 8;

    static void encode(int value, byte separator, final ByteBuf buffer) {
        if (value < 0)
            negativeMedium();

        long bits = separator;
        int length = 1;

        do {
            bits <<= 8;
            bits |= '0' + value % 10;

            value /= 10;
            length++;
        } while (value > 0);

        switch (length) {
        case 2:
            buffer.writeShortLE((int)bits);
            break;
        case 3:
            buffer.writeMediumLE((int)bits);
            break;
        case 4:
            buffer.writeIntLE((int)bits);
            break;
        case 5:
            buffer.writeIntLE((int)bits);
            buffer.writeByte((int)(bits >> 32));
            break;
        case 6:
            buffer.writeIntLE((int)bits);
            buffer.writeShortLE((int)(bits >> 32));
            break;
        case 7:
            buffer.writeIntLE((int)bits);
            buffer.writeMediumLE((int)(bits >> 32));
            break;
        case 8:
            buffer.writeLongLE(bits);
            break;
        default:
            tooLargeMedium();
        }
    }

    private static void negativeMedium() {
        throw new IllegalArgumentException("Negative integer");
    }

    private static void tooLargeMedium() {
        throw new IllegalArgumentException("Too large integer");
    }

}
