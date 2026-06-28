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

class FixDateDecoder {

    private static final VarHandle LONG = MethodHandles.byteArrayViewVarHandle(long[].class, LITTLE_ENDIAN);

    static void decode(final byte[] bytes, final int index, final int length, final FixDate container) {
        if (length != 8)
            notDate();

        try {
            decode(bytes, index, container);
        } catch (IllegalArgumentException e) {
            notDate();
        }
    }

    static void decode(final byte[] bytes, final int index, final FixDate container) {

        // "20260627" = 0x3732363036323032
        long bits = (long)LONG.get(bytes, index);

        // "20260627" = 0x0702060006020002
        bits -= 0x3030303030303030l;

        // 0x7f - 0x09 = 0x76
        if (((bits | (bits + 0x7676767676767676l)) & 0x8080808080808080l) != 0)
            illegalArgument();

        // "20260627" = 0x001b0006001a0014
        bits = (10 * bits + (bits >>> 8)) & 0x00ff00ff00ff00ffl;

        int yearHigh = (int)(bits & 0xff);
        int yearLow = (int)((bits >>> 16) & 0xff);
        int month = (int)((bits >>> 32) & 0xff);
        int day = (int)((bits >>> 48) & 0xff);

        container
            .setYear(100 * yearHigh + yearLow)
            .setMonth(month)
            .setDay(day);
    }

    private static void illegalArgument() {
        throw new IllegalArgumentException();
    }

    private static void notDate() {
        throw new FixDecoderException("Not a UTCDate or LocalMktDate");
    }

}
