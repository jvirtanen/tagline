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
import static org.jvirtanen.tagline.codec.FixTimeFormat.*;

import io.netty.buffer.ByteBuf;

class FixTimeEncoder {

    static void encode(final FixTime time, final FixTimeFormat format, final ByteBuf buffer) {
        buffer.writeLongLE(TWO_DIGITS[time.hour()]
                | (long)':' << 16
                | (long)TWO_DIGITS[time.minute()] << 24
                | (long)':' << 40
                | (long)TWO_DIGITS[time.second()] << 48);

        if (format != WITH_MILLISECONDS)
            return;

        int milli = time.milli();

        buffer.writeIntLE('.'
                | ('0' + milli / 100) << 8
                | (int)TWO_DIGITS[milli % 100] << 16);
    }

}
