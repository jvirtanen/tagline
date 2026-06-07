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

class FixVersionDecoder {

    private FixVersion version;

    FixVersionDecoder() {
        version = null;
    }

    FixVersion version() {
        return version;
    }

    int decode(final ByteBuf buffer, final int offset, final int length) {
        if (version != null && version.matches(buffer, offset, length))
            return offset + version.length();
        else
            return setVersion(buffer, offset, length);
    }

    private int setVersion(final ByteBuf buffer, final int offset, final int length) {
        int sohIndex = buffer.indexOf(offset, offset + length, SOH);
        if (sohIndex < 0)
            return sohIndex;

        if (sohIndex == offset)
            notBeginString();

        version = FixVersion.of(buffer, offset, sohIndex + 1 - offset);

        return sohIndex + 1;
    }

    private static void notBeginString() {
        throw new FixDecoderException("Not a BeginString(8) value");
    }

}
