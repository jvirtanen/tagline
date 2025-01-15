/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static io.netty.util.internal.ObjectPool.*;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.ObjectPool;

class ReferenceCountedFixFieldList extends FixFieldList implements ReferenceCounted {

    private static final ObjectPool<ReferenceCountedFixFieldList> POOL = ObjectPool.newPool(
        new ObjectCreator<>() {

            @Override
            public ReferenceCountedFixFieldList newObject(final Handle<ReferenceCountedFixFieldList> handle) {
                return new ReferenceCountedFixFieldList(handle);
            }

    });

    private final Handle<ReferenceCountedFixFieldList> handle;
    private final ReferenceCounted refCnt = new AbstractReferenceCounted() {

        @Override
        public ReferenceCountedFixFieldList touch(final Object hint) {
            return ReferenceCountedFixFieldList.this;
        }

        @Override
        protected void deallocate() {
            clear();

            handle.recycle(ReferenceCountedFixFieldList.this);

            setRefCnt(1);
        }

    };

    static ReferenceCountedFixFieldList newInstance() {
        return POOL.get();
    }

    ReferenceCountedFixFieldList(final Handle<ReferenceCountedFixFieldList> handle) {
        this.handle = handle;
    }

    @Override
    public int refCnt() {
        return refCnt.refCnt();
    }

    @Override
    public ReferenceCountedFixFieldList retain() {
        refCnt.retain();

        return this;
    }

    @Override
    public ReferenceCountedFixFieldList retain(final int increment) {
        refCnt.retain(increment);

        return this;
    }

    @Override
    public ReferenceCountedFixFieldList touch() {
        refCnt.touch();

        return this;
    }

    @Override
    public ReferenceCountedFixFieldList touch(final Object hint) {
        refCnt.touch(hint);

        return this;
    }

    @Override
    public boolean release() {
        return refCnt.release();
    }

    @Override
    public boolean release(final int decrement) {
        return refCnt.release(decrement);
    }

}
