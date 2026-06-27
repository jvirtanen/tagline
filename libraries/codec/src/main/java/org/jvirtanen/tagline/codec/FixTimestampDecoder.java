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

class FixTimestampDecoder {

    static void decode(final byte[] bytes, final int index, final int length, final FixTimestamp container) {
        if (length == 21)
            decodeMilli(bytes, index, container);
        else if (length == 17)
            decodeSecond(bytes, index, container);
        else
            notTimestamp();
    }

    private static void decodeMilli(final byte[] bytes, final int index, final FixTimestamp container) {
        if (bytes[index + 8] != '-')
            notTimestamp();

        try {
            FixDateDecoder.decode(bytes, index, container);
            FixTimeDecoder.decodeSecond(bytes, index + 9, container);
            FixTimeDecoder.decodeMilli(bytes, index + 17, container);
        } catch (IllegalArgumentException e) {
            notTimestamp();
        }
    }

    private static void decodeSecond(final byte[] bytes, final int index, final FixTimestamp container) {
        if (bytes[index + 8] != '-')
            notTimestamp();

        try {
            FixDateDecoder.decode(bytes, index, container);
            FixTimeDecoder.decodeSecond(bytes, index + 9, container);
            container.setMilli(0);
        } catch (IllegalArgumentException e) {
            notTimestamp();
        }
    }

    private static void notTimestamp() {
        throw new FixDecoderException("Not a UTCTimestamp");
    }

}
