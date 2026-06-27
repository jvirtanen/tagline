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

import static java.nio.ByteOrder.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

class FixTimeDecoder {

    private static final VarHandle LONG = MethodHandles.byteArrayViewVarHandle(long[].class, LITTLE_ENDIAN);
    private static final VarHandle INT = MethodHandles.byteArrayViewVarHandle(int[].class, LITTLE_ENDIAN);

    static void decode(final byte[] bytes, final int index, final int length, final FixTime container) {
        try {
            if (length == 12) {
                decodeSecond(bytes, index, container);
                decodeMilli(bytes, index + 8, container);
            } else if (length == 8) {
                decodeSecond(bytes, index, container);
                container.setMilli(0);
            } else {
                notTime();
            }
        } catch (IllegalArgumentException e) {
            notTime();
        }
    }

    static void decodeSecond(final byte[] bytes, final int index, final FixTime container) {

        // "22:05:30" = 0x30333a35303a3232
        long bits = (long)LONG.get(bytes, index);

        // ':' = 0x3a
        if (((bits ^ 0x00003a00003a0000l) & 0x0000ff0000ff0000l) != 0)
            illegalArgument();

        // "22:05:30" = 0x0003000500000202
        bits -= 0x30303a30303a3030l;

        // 0x7f - 0x09 = 0x76
        if (((bits | (bits + 0x7676767676767676l)) & 0x8080808080808080l) != 0)
            illegalArgument();

        // "22:05:30" = 0x001e000005000016
        bits = (10 * bits + (bits >>> 8)) & 0x00ff0000ff0000ffl;

        int hour = (int)(bits & 0xff);
        int minute = (int)((bits >>> 24) & 0xff);
        int second = (int)((bits >>> 48) & 0xff);

        container.setHour(hour);
        container.setMinute(minute);
        container.setSecond(second);
    }

    static void decodeMilli(final byte[] bytes, final int index, final FixTime container) {

        // ".250" = 0x3035322e
        int bits = (int)INT.get(bytes, index);

        // '.' = 0x2e
        if (((bits ^ 0x0000002e) & 0x000000ff) != 0)
            illegalArgument();

        // ".250" = 0x00050200
        bits -= 0x3030302e;

        // 0x7f - 0x09 = 0x76
        if (((bits | (bits + 0x76767676)) & 0x80808080) != 0)
            illegalArgument();

        // ".250" = 0x00001900
        bits = (10 * bits + (bits >>> 8)) & 0xff00ff00;

        int milliHigh = (int)((bits >>> 8) & 0xff);
        int milliLow = (int)((bits >>> 24) & 0xff);

        container.setMilli(10 * milliHigh + milliLow);
    }

    private static void illegalArgument() {
        throw new IllegalArgumentException();
    }

    private static void notTime() {
        throw new FixDecoderException("Not a UTCTimeOnly");
    }

}
