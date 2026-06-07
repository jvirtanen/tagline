/*
 * Copyright 2025 Jussi Virtanen
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

import static io.netty.util.Recycler.*;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;

/**
 * A pooled FIX field list.
 */
public class PooledFixFieldList extends DefaultFixFieldList implements ReferenceCounted {

    private final Handle<PooledFixFieldList> handle;
    private final ReferenceCounted refCnt = new AbstractReferenceCounted() {

        @Override
        public PooledFixFieldList touch(final Object hint) {
            return PooledFixFieldList.this;
        }

        @Override
        protected void deallocate() {
            clear();

            handle.recycle(PooledFixFieldList.this);

            setRefCnt(1);
        }

    };

    PooledFixFieldList(final Handle<PooledFixFieldList> handle, final FixFieldListConfig config) {
        super(config);

        this.handle = handle;
    }

    /**
     * Get the reference count.
     *
     * @return the reference count
     */
    @Override
    public int refCnt() {
        return refCnt.refCnt();
    }

    /**
     * Increase the reference count by 1.
     *
     * @return this instance
     */
    @Override
    public PooledFixFieldList retain() {
        refCnt.retain();

        return this;
    }

    /**
     * Increase the reference count by the specified increment.
     *
     * @param increment the increment
     * @return this instance
     */
    @Override
    public PooledFixFieldList retain(final int increment) {
        refCnt.retain(increment);

        return this;
    }

    /**
     * Record the current access location for debugging purposes.
     *
     * @return this instance
     */
    @Override
    public PooledFixFieldList touch() {
        refCnt.touch();

        return this;
    }

    /**
     * Record the current access location for debugging purposes.
     *
     * @param hint the hint
     * @return this instance
     */
    @Override
    public PooledFixFieldList touch(final Object hint) {
        refCnt.touch(hint);

        return this;
    }

    /**
     * Decrease the reference count by 1.
     *
     * @return true if the reference count became 0 and this instance was
     *     deallocated, otherwise false
     */
    @Override
    public boolean release() {
        return refCnt.release();
    }

    /**
     * Decrease the reference count by the specified decrement.
     *
     * @param decrement the decrement
     * @return true if the reference count became 0 and this instance was
     *     deallocated, otherwise false
     */
    @Override
    public boolean release(final int decrement) {
        return refCnt.release(decrement);
    }

}
