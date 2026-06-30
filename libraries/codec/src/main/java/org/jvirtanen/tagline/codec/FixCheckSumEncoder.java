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

import static org.jvirtanen.tagline.codec.AsciiUtil.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

class FixCheckSumEncoder {

    static void encode(final int checkSum, final ByteBuf buffer) {
        int writerIndex = buffer.writerIndex();

        buffer.ensureWritable(8);
        buffer.setLongLE(writerIndex, '1'
                | (long)'0' << 8
                | (long)EQUALS << 16
                | (long)('0' + checkSum / 100 % 10) << 24
                | (long)TWO_DIGITS[checkSum % 100] << 32
                | (long)SOH << 48);
        buffer.writerIndex(writerIndex + 7);
    }

}
