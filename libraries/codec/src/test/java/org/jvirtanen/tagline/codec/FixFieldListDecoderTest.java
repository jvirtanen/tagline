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

import static io.netty.buffer.Unpooled.*;
import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class FixFieldListDecoderTest {

    @Test
    void emptyBody() {
        var buffer = copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 0, 198);

        var fields = decode(message);

        assertEquals("8=FIX.4.2|9=0|10=198|", fields.toString());
    }

    @Test
    void nonEmptyBody() {
        var buffer = copiedBuffer("8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 5, 181);

        var fields = decode(message);

        assertEquals("8=FIX.4.2|9=5|35=D|10=181|", fields.toString());
    }

    @Test
    void invalidBody() {
        var buffer = copiedBuffer("8=FIX.4.2\u00019=3\u0001FOO10=173\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 3, 173);

        assertThrows(GarbledFixMessageException.class, () -> decode(message));
    }

    @Test
    void garbled() {
        var message = new DefaultInboundFixMessage(null, EMPTY_BUFFER, 0, 0, 0).setGarbled();

        var exception = assertThrows(GarbledFixMessageException.class, () -> decode(message));

        assertEquals("Invalid message", exception.getMessage());
    }

    @Test
    void validVersion() {
        var config = FixFieldListDecoderConfig.newBuilder()
            .setVersion(FIX_4_2)
            .build();

        var buffer = copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 0, 198);

        var fields = decode(message, config);

        assertEquals("8=FIX.4.2|9=0|10=198|", fields.toString());
    }

    @Test
    void invalidVersion() {
        var config = FixFieldListDecoderConfig.newBuilder()
            .setVersion(FIX_4_2)
            .build();

        var buffer = copiedBuffer("8=FIX.4.3\u00019=0\u000110=198\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_3, buffer, 14, 0, 199);

        var exception = assertThrows(GarbledFixMessageException.class, () -> decode(message, config));

        assertEquals("Unexpected BeginString(8) value \"FIX.4.3\", expected \"FIX.4.2\"",
                exception.getMessage());
    }

    @Test
    void validCheckSum() {
        var config = FixFieldListDecoderConfig.newBuilder()
            .setCheckSumEnabled(true)
            .build();

        var buffer = copiedBuffer("8=FIX.4.2\u00019=0\u000110=198\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 0, 198);

        var fields = decode(message, config);

        assertEquals("8=FIX.4.2|9=0|10=198|", fields.toString());
    }

    @Test
    void invalidCheckSum() {
        var config = FixFieldListDecoderConfig.newBuilder()
            .setCheckSumEnabled(true)
            .build();

        var buffer = copiedBuffer("8=FIX.4.2\u00019=0\u000110=999\u0001");
        var message = new DefaultInboundFixMessage(FIX_4_2, buffer, 14, 0, 999);

        var exception = assertThrows(GarbledFixMessageException.class, () -> decode(message, config));

        assertEquals("Unexpected CheckSum(10) value 999, expected 198", exception.getMessage());
    }

    private static FixFieldList decode(final InboundFixMessage in) {
        return decode(in, FixFieldListDecoderConfig.DEFAULTS);
    }

    private static FixFieldList decode(final InboundFixMessage in, final FixFieldListDecoderConfig config) {
        var channel = new EmbeddedChannel(new FixFieldListDecoder(config));

        channel.writeInbound(in);

        var out = new ArrayList<FixFieldList>();

        while (true) {
            FixFieldList message = channel.readInbound();
            if (message == null)
                break;

            out.add(message);
        }

        channel.finish();

        assertEquals(1, out.size());

        return out.get(0);
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

}
