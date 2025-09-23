/*
 * Copyright 2025 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static io.netty.util.Recycler.*;

import io.netty.util.Recycler;

/**
 * A FIX field list pool.
 */
public class FixFieldListPool {

    private final Recycler<PooledFixFieldList> pool;

    /**
     * Construct a new instance using the default configuration.
     */
    public FixFieldListPool() {
        this(FixFieldListConfig.DEFAULTS);
    }

    /**
     * Construct a new instance.
     *
     * @param config the configuration
     */
    public FixFieldListPool(final FixFieldListConfig config) {
        pool = new Recycler<>() {

            @Override
            public PooledFixFieldList newObject(final Handle<PooledFixFieldList> handle) {
                return new PooledFixFieldList(handle, config);
            }

        };
    }

    /**
     * Get a pooled FIX field list.
     *
     * @return a pooled FIX field list
     */
    public PooledFixFieldList get() {
        return pool.get();
    }

}
