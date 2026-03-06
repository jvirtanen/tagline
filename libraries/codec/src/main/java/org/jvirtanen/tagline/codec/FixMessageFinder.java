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

class FixMessageFinder implements ByteProcessor {

    private byte nextValue;

    int find(final ByteBuf buffer, int index, int length) {
        nextValue = SOH;

        int equalsIndex = buffer.forEachByte(index, length, this);
        if (equalsIndex < 0)
            return equalsIndex;

        return equalsIndex - 1;
    }

    @Override
    public boolean process(final byte value) {
        if (value != nextValue) {
            nextValue = SOH;

            return true;
        }

        switch (value) {
        case SOH:
            nextValue = '8';
            break;
        case '8':
            nextValue = EQUALS;
            break;
        case EQUALS:
            return false;
        }

        return true;
    }

}
