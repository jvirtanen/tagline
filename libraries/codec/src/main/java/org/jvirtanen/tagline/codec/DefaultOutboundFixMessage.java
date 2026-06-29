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
import static org.jvirtanen.tagline.codec.FixTimeFormat.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * The default implementation of an outbound FIX message.
 */
public class DefaultOutboundFixMessage extends DefaultFixMessage implements OutboundFixMessage {

    private static final FastThreadLocal<FixIntEncoder> INT_ENCODER = new FastThreadLocal<>() {

        @Override
        protected FixIntEncoder initialValue() {
            return new FixIntEncoder();
        }

    };

    private static final FastThreadLocal<DefaultFixTimestamp> TIMESTAMP = new FastThreadLocal<>() {

        @Override
        protected DefaultFixTimestamp initialValue() {
            return new DefaultFixTimestamp();
        }

    };

    private static final FastThreadLocal<FixFloatEncoder> FLOAT_ENCODER = new FastThreadLocal<>() {

        @Override
        protected FixFloatEncoder initialValue() {
            return new FixFloatEncoder();
        }

    };

    private static final FastThreadLocal<DefaultFixFloat> FLOAT = new FastThreadLocal<>() {

        @Override
        protected DefaultFixFloat initialValue() {
            return new DefaultFixFloat();
        }

    };

    /**
     * Construct a new instance.
     *
     * @param version the FIX protocol version
     * @param allocator a buffer allocator
     */
    public DefaultOutboundFixMessage(final FixVersion version, final ByteBufAllocator allocator) {
        this(version, allocator.ioBuffer());
    }

    /**
     * Construct a new instance.
     *
     * @param version the FIX protocol version
     * @param content the content
     */
    public DefaultOutboundFixMessage(final FixVersion version, final ByteBuf content) {
        super(version, content);
    }

    @Override
    public DefaultOutboundFixMessage addBoolean(final int tag, final boolean value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        FixBooleanEncoder.encode(value, content);

        return this;
    }

    public DefaultOutboundFixMessage addChar(final int tag, final char value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        FixCharEncoder.encode(value, content);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addInt(final int tag, final long value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        INT_ENCODER.get().encode(value, content);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addFloat(final int tag, final FixFloat value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        FLOAT_ENCODER.get().encode(value, content);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addFloat(final int tag, final double value, final int scale) {
        return addFloat(tag, FLOAT.get().setValue(value, scale));
    }

    @Override
    public DefaultOutboundFixMessage addString(final int tag, final CharSequence value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        ByteBufUtil.writeAscii(content, value);
        content.writeByte(SOH);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addDate(final int tag, final FixDate value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        FixDateEncoder.encode(value, content);
        content.writeByte(SOH);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addTime(final int tag, final FixTime value, final FixTimeFormat format) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        FixTimeEncoder.encode(value, format, content);
        content.writeByte(SOH);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addTimestamp(final int tag, final FixTimestamp value, final FixTimeFormat format) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        FixTimestampEncoder.encode(value, format, content);
        content.writeByte(SOH);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage addTimestamp(final int tag, final long value, final FixTimeFormat format) {
        var timestamp = TIMESTAMP.get();

        timestamp.setEpochMilli(value);

        return addTimestamp(tag, timestamp, format);
    }

    @Override
    public DefaultOutboundFixMessage addBytes(final int tag, final byte[] value) {
        FixMediumEncoder.encode(tag, EQUALS, content);
        content.writeBytes(value);
        content.writeByte(SOH);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage copy() {
        return replace(content.copy());
    }

    @Override
    public DefaultOutboundFixMessage duplicate() {
        return replace(content.duplicate());
    }

    @Override
    public DefaultOutboundFixMessage retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    @Override
    public DefaultOutboundFixMessage replace(final ByteBuf content) {
        return new DefaultOutboundFixMessage(version(), content);
    }

    @Override
    public DefaultOutboundFixMessage retain() {
        super.retain();

        return this;
    }

    @Override
    public DefaultOutboundFixMessage retain(final int increment) {
        super.retain(increment);

        return this;
    }

    @Override
    public DefaultOutboundFixMessage touch() {
        super.touch();

        return this;
    }

    @Override
    public DefaultOutboundFixMessage touch(final Object hint) {
        super.touch(hint);

        return this;
    }

}
