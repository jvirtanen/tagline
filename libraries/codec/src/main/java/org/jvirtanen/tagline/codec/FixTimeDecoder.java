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

import static org.jvirtanen.tagline.codec.ByteArrayDecoder.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

class FixTimeDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixTime container) {
        if (length != 8 && length != 12)
            notTime();

        try {
            container.setHour(decodeTwoDigits(bytes, index));

            if (bytes[index + 2] != ':')
                notTime();

            container.setMinute(decodeTwoDigits(bytes, index + 3));

            if (bytes[index + 5] != ':')
                notTime();

            container.setSecond(decodeTwoDigits(bytes, index + 6));

            if (length == 12) {
                if (bytes[index + 8] != '.')
                    notTime();

                container.setMilli(decodeThreeDigits(bytes, index + 9));
            } else {
                container.setMilli(0);
            }
        } catch (IllegalArgumentException e) {
            notTime();
        }
    }

    private static void notTime() {
        throw new FixDecoderException("Not a UTCTimeOnly");
    }

}
