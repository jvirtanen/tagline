/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * The inbound FIX message decoder configuration.
 */
public class InboundFixMessageDecoderConfig {

    /**
     * The default maximum BodyLength(9) value: {@link Integer#MAX_VALUE} bytes (roughly 2 GiB).
     */
    public static final int DEFAULT_MAX_BODY_LENGTH = Integer.MAX_VALUE;

    /**
     * The defaults.
     */
    public static final InboundFixMessageDecoderConfig DEFAULTS = newBuilder().build();

    private final int maxBodyLength;

    /**
     * Construct a new instance.
     *
     * @param maxBodyLength the maximum BodyLength(9) value
     */
    public InboundFixMessageDecoderConfig(final int maxBodyLength) {
        this.maxBodyLength = maxBodyLength;
    }

    /**
     * Get the maximum BodyLength(9) value.
     *
     * @return the maximum BodyLength(9) value
     */
    public int maxBodyLength() {
        return maxBodyLength;
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
     * An inbound FIX message decoder configuration builder.
     */
    public static class Builder {

        private int maxBodyLength;

        private Builder() {
            maxBodyLength = DEFAULT_MAX_BODY_LENGTH;
        }

        /**
         * Set the maximum BodyLength(9) value.
         *
         * @param maxBodyLength the maximum BodyLength(9) value
         * @return this instance
         */
        public Builder setMaxBodyLength(final int maxBodyLength) {
            this.maxBodyLength = maxBodyLength;

            return this;
        }

        /**
         * Build a configuration.
         *
         * @return a configuration
         */
        public InboundFixMessageDecoderConfig build() {
            return new InboundFixMessageDecoderConfig(maxBodyLength);
        }

    }

}
