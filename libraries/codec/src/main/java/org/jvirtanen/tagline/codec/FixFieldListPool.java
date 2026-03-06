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
