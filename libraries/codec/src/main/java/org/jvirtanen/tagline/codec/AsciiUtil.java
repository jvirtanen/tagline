/*
 * Copyright 2026 Jussi Virtanen
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

class AsciiUtil {

    static final short[] TWO_DIGITS;

    static {
        TWO_DIGITS = new short[100];

        for (int i = 0; i < 100; i++)
            TWO_DIGITS[i] = (short)(('0' + i / 10) | (('0' + i % 10) << 8));
    }
}
