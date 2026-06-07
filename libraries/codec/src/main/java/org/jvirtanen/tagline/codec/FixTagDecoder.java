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

class FixTagDecoder {

    private int tag;

    int tag() {
        return tag;
    }

    int decode(final byte[] bytes, int index, final int length) {
        byte b = bytes[index++];
        if (b < '1' || b > '9')
            invalidTag();

        tag = b - '0';

        while (index < length) {
            b = bytes[index++];
            if (b == EQUALS)
                break;

            if (b < '0' || b > '9')
                invalidTag();

            tag = 10 * tag + b - '0';
            if (tag < 0)
                invalidTag();
        }

        if (b != EQUALS)
            return -1;

        return index;
    }

    private static void invalidTag() {
        throw new FixDecoderException("Invalid tag");
    }

}
