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

class FixCheckSumDecoder {

    static int decode(final ByteBuf buffer, final int index) {
        int bits = buffer.getInt(index);

        int a = ((bits & 0xff000000) >> 24) - '0';
        int b = ((bits & 0xff0000) >> 16) - '0';
        int c = ((bits & 0xff00) >> 8) - '0';
        int d = bits & 0xff;

        if (a < 0 || a > 9 || b < 0 || b > 9 || c < 0 || c > 9 || d != SOH)
            notCheckSum();

        return 100 * a + 10 * b + c;
    }

    private static void notCheckSum() {
        throw new FixDecoderException("Invalid CheckSum(10) value");
    }

}
