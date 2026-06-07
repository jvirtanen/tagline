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

class FixDateDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixDate container) {
        if (length != 8)
            notDate();

        try {
            container
                .setYear(decodeFourDigits(bytes, index))
                .setMonth(decodeTwoDigits(bytes, index + 4))
                .setDay(decodeTwoDigits(bytes, index + 6));
        } catch (IllegalArgumentException e) {
            notDate();
        }
    }

    private static void notDate() {
        throw new FixDecoderException("Not a UTCDate or LocalMktDate");
    }

}
