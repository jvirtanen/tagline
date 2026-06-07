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

abstract class DefaultFixMessage implements FixMessage {

    private final FixVersion version;

    /**
     * The content.
     */
    protected final ByteBuf content;

    DefaultFixMessage(final FixVersion version, final ByteBuf content) {
        this.version = version;
        this.content = content;
    }

    @Override
    public FixVersion version() {
        return version;
    }

    @Override
    public ByteBuf content() {
        return content;
    }

    @Override
    public int refCnt() {
        return content.refCnt();
    }

    @Override
    public DefaultFixMessage copy() {
        return replace(content.copy());
    }

    @Override
    public DefaultFixMessage duplicate() {
        return replace(content.duplicate());
    }

    @Override
    public DefaultFixMessage retainedDuplicate() {
        return replace(content.retainedDuplicate());
    }

    @Override
    public abstract DefaultFixMessage replace(final ByteBuf content);

    @Override
    public DefaultFixMessage retain() {
        content.retain();

        return this;
    }

    @Override
    public DefaultFixMessage retain(final int increment) {
        content.retain(increment);

        return this;
    }

    @Override
    public boolean release() {
        return content.release();
    }

    @Override
    public boolean release(final int decrement) {
        return content.release(decrement);
    }

    @Override
    public DefaultFixMessage touch() {
        content.touch();

        return this;
    }

    @Override
    public DefaultFixMessage touch(final Object hint) {
        content.touch(hint);

        return this;
    }

}
