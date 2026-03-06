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

class FixDateEncoder {

    static void encode(final FixDate date, final ByteBuf buffer) {
        long year = date.year();
        long month = date.month();
        long day = date.day();

        buffer.writeLong(('0' + year / 1000) << 56
                | ('0' + year / 100 % 10) << 48
                | ('0' + year / 10 % 10) << 40
                | ('0' + year % 10) << 32
                | ('0' + month / 10) << 24
                | ('0' + month % 10) << 16
                | ('0' + day / 10) << 8
                | ('0' + day % 10));
    }

}
