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

class FixTimestampDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixTimestamp container) {
        if (length != 17 && length != 21)
            notTimestamp();

        try {
            container
                .setYear(decodeFourDigits(bytes, index))
                .setMonth(decodeTwoDigits(bytes, index + 4))
                .setDay(decodeTwoDigits(bytes, index + 6));

            if (bytes[index + 8] != '-')
                notTimestamp();

            container.setHour(decodeTwoDigits(bytes, index + 9));

            if (bytes[index + 11] != ':')
                notTimestamp();

            container.setMinute(decodeTwoDigits(bytes, index + 12));

            if (bytes[index + 14] != ':')
                notTimestamp();

            container.setSecond(decodeTwoDigits(bytes, index + 15));

            if (length == 21) {
                if (bytes[index + 17] != '.')
                    notTimestamp();

                container.setMilli(decodeThreeDigits(bytes, index + 18));
            } else {
                container.setMilli(0);
            }
        } catch (IllegalArgumentException e) {
            notTimestamp();
        }
    }

    private static void notTimestamp() {
        throw new FixDecoderException("Not a UTCTimestamp");
    }

}
