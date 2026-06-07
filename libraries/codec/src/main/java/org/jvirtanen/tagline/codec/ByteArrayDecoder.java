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

class ByteArrayDecoder {

    static int decodeDigit(final byte[] bytes, final int index) {
        int b = bytes[index];
        if (b < '0' || b > '9')
            notDigit();

        return b - '0';
    }

    static int decodeTwoDigits(final byte[] bytes, final int index) {
        return 10 * decodeDigit(bytes, index) + decodeDigit(bytes, index + 1);
    }

    static int decodeThreeDigits(final byte[] bytes, final int index) {
        return 100 * decodeDigit(bytes, index) + 10 * decodeDigit(bytes, index + 1)
            + decodeDigit(bytes, index + 2);
    }

    static int decodeFourDigits(final byte[] bytes, final int index) {
        return 1000 * decodeDigit(bytes, index) + 100 * decodeDigit(bytes, index + 1)
            + 10 * decodeDigit(bytes, index + 2) + decodeDigit(bytes, index + 3);
    }

    private static void notDigit() {
        throw new IllegalArgumentException("Not a digit");
    }

}
