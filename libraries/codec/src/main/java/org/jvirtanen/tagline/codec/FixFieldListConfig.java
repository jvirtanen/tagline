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

/**
 * The FIX field list configuration.
 */
public class FixFieldListConfig {

    /**
     * The default initial buffer size: 16 KiB.
     */
    public static final int DEFAULT_INITIAL_BUFFER_SIZE = 16384;

    /**
     * The default initial capacity: 256.
     */
    public static final int DEFAULT_INITIAL_CAPACITY = 256;

    /**
     * The defaults.
     */
    public static final FixFieldListConfig DEFAULTS = newBuilder().build();

    private final int initialBufferSize;
    private final int initialCapacity;

    /**
     * Construct a new instance.
     *
     * @param initialBufferSize the initial buffer size
     * @param initialCapacity the initial capacity
     */
    public FixFieldListConfig(final int initialBufferSize, final int initialCapacity) {
        this.initialBufferSize = initialBufferSize;
        this.initialCapacity = initialCapacity;
    }

    /**
     * Get the initial buffer size.
     *
     * @return the initial buffer size
     */
    public int initialBufferSize() {
        return initialBufferSize;
    }

    /**
     * Get the initial capacity.
     *
     * @return the initial capacity
     */
    public int initialCapacity() {
        return initialCapacity;
    }

    /**
     * Construct a new builder.
     *
     * @return a new builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * A FIX field list configuration builder.
     */
    public static class Builder {

        private int initialBufferSize;
        private int initialCapacity;

        private Builder() {
            initialBufferSize = DEFAULT_INITIAL_BUFFER_SIZE;
            initialCapacity = DEFAULT_INITIAL_CAPACITY;
        }

        /**
         * Set the initial buffer size.
         *
         * @param initialBufferSize the initial buffer size
         * @return this instance
         */
        public Builder setInitialBufferSize(final int initialBufferSize) {
            this.initialBufferSize = initialBufferSize;

            return this;
        }

        /**
         * Set the initial capacity.
         *
         * @param initialCapacity the initial capacity
         * @return this instance
         */
        public Builder setInitialCapacity(final int initialCapacity) {
            this.initialCapacity = initialCapacity;

            return this;
        }

        /**
         * Build a configuration.
         *
         * @return a configuration
         */
        public FixFieldListConfig build() {
            return new FixFieldListConfig(initialBufferSize, initialCapacity);
        }

    }

}
