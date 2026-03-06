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

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class DefaultInboundFixMessageTest {

    @Test
    void setGarbled() {
        var content = copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, content, 14, 0, 198);

        message.setGarbled();

        assertTrue(message.isGarbled());
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

}
