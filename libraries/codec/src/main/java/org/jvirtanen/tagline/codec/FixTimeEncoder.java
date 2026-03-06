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
import static org.jvirtanen.tagline.codec.FixTimeFormat.*;

import io.netty.buffer.ByteBuf;

class FixTimeEncoder {

    static void encode(final FixTime time, final FixTimeFormat format, final ByteBuf buffer) {
        long hour = time.hour();
        long minute = time.minute();
        long second = time.second();

        buffer.writeLong(('0' + hour / 10) << 56
                | ('0' + hour % 10) << 48
                | (long)':' << 40
                | ('0' + minute / 10) << 32
                | ('0' + minute % 10) << 24
                | (long)':' << 16
                | ('0' + second / 10) << 8
                | ('0' + second % 10));

        if (format != WITH_MILLISECONDS)
            return;

        int milli = time.milli();

        buffer.writeInt('.' << 24
                | ('0' + milli / 100) << 16
                | ('0' + milli / 10 % 10) << 8
                | ('0' + milli % 10));
    }

}
