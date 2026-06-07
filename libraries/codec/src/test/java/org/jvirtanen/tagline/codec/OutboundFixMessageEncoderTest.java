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
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutboundFixMessageEncoderTest {

    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        channel = new EmbeddedChannel(new OutboundFixMessageEncoder());
    }

    @AfterEach
    void tearDown() {
        channel.finish();
    }

    @Test
    void empty() {
        var message = new DefaultOutboundFixMessage(FIX_4_2, Unpooled.buffer());

        channel.writeOutbound(message);

        assertOutbound("8=FIX.4.2\u00019=0\u0001");
        assertOutbound("10=198\u0001");
    }

    @Test
    void nonEmpty() {
        var message = new DefaultOutboundFixMessage(FIX_4_2, Unpooled.buffer());

        message.addChar(35, 'D');

        channel.writeOutbound(message);

        assertOutbound("8=FIX.4.2\u00019=5\u0001");
        assertOutbound("35=D\u000110=181\u0001");
    }

    private void assertOutbound(final String expected) {
        ByteBuf actual = channel.readOutbound();

        assertEquals(expected, actual.toString(ISO_8859_1));
    }

}
