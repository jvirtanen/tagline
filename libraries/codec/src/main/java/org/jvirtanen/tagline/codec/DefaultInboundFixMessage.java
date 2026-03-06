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

import io.netty.buffer.ByteBuf;

/**
 * The default implementation of an inbound FIX message.
 */
public class DefaultInboundFixMessage extends DefaultFixMessage implements InboundFixMessage {

    private final int bodyOffset;
    private final int bodyLength;
    private final int checkSum;

    private boolean isGarbled;

    /**
     * Construct a new non-garbled instance.
     *
     * @param version the version
     * @param content the content
     * @param bodyOffset the body offset in the content
     * @param bodyLength the BodyLength(9) value
     * @param checkSum the CheckSum(10) value
     */
    public DefaultInboundFixMessage(final FixVersion version, final ByteBuf content,
            final int bodyOffset, final int bodyLength, final int checkSum) {
        this(version, content, bodyOffset, bodyLength, checkSum, false);
    }

    /**
     * Construct a new garbled instance.
     *
     * @param content the content
     */
    public DefaultInboundFixMessage(final ByteBuf content) {
        this(null, content, -1, -1, -1, true);
    }

    private DefaultInboundFixMessage(final FixVersion version, final ByteBuf content,
            final int bodyOffset, final int bodyLength, final int checkSum, final boolean isGarbled) {
        super(version, content);

        this.bodyOffset = bodyOffset;
        this.bodyLength = bodyLength;
        this.checkSum = checkSum;

        this.isGarbled = isGarbled;
    }

    @Override
    public boolean isGarbled() {
        return isGarbled;
    }

    @Override
    public DefaultInboundFixMessage setGarbled() {
        this.isGarbled = true;

        return this;
    }

    @Override
    public int bodyOffset() {
        return bodyOffset;
    }

    @Override
    public int bodyLength() {
        return bodyLength;
    }

    @Override
    public int checkSum() {
        return checkSum;
    }

    @Override
    public DefaultInboundFixMessage copy() {
        return replace(content.copy());
    }

    @Override
    public DefaultInboundFixMessage duplicate() {
        return replace(content.duplicate());
    }

    @Override
    public DefaultInboundFixMessage retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    @Override
    public DefaultInboundFixMessage replace(final ByteBuf content) {
        return new DefaultInboundFixMessage(version(), content, bodyOffset, bodyLength, checkSum);
    }

    @Override
    public DefaultInboundFixMessage retain() {
        super.retain();

        return this;
    }

    @Override
    public DefaultInboundFixMessage retain(final int increment) {
        super.retain(increment);

        return this;
    }

    @Override
    public DefaultInboundFixMessage touch() {
        super.touch();

        return this;
    }

    @Override
    public DefaultInboundFixMessage touch(final Object hint) {
        super.touch(hint);

        return this;
    }

}
