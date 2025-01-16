/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * The FIX field list decoder configuration.
 */
public class FixFieldListDecoderConfig {

    /**
     * The default BeginString(8) checking policy: disabled.
     */
    public static final FixVersion DEFAULT_VERSION = null;

    /**
     * The default CheckSum(10) checking policy: disabled.
     */
    public static final boolean DEFAULT_CHECK_SUM_ENABLED = false;

    /**
     * The default message configuration: {@link FixFieldListConfig#DEFAULTS}.
     */
    public static final FixFieldListConfig DEFAULT_MESSAGE_CONFIG = FixFieldListConfig.DEFAULTS;

    /**
     * The defaults.
     */
    public static final FixFieldListDecoderConfig DEFAULTS = newBuilder().build();

    private final FixVersion version;
    private final boolean isCheckSumEnabled;
    private final FixFieldListConfig messageConfig;

    /**
     * Construct a new instance.
     *
     * @param version the FIX protocol version or {@code null} to disable the
     *     BeginString(8) check
     * @param isCheckSumEnabled true if the CheckSum(10) check enabled,
     *     otherwise false
     * @param messageConfig the message configuration
     */
    public FixFieldListDecoderConfig(final FixVersion version, final boolean isCheckSumEnabled,
            final FixFieldListConfig messageConfig) {
        this.version = version;
        this.isCheckSumEnabled = isCheckSumEnabled;
        this.messageConfig = messageConfig;
    }

    /**
     * Get the FIX protocol version.
     *
     * @return the FIX protocol version or {@code null} to disable the
     *     BeginString(8) check
     */
    public FixVersion version() {
        return version;
    }

    /**
     * Get the CheckSum(10) checking policy.
     *
     * @return true if the CheckSum(10) check is enabled, otherwise false
     */
    public boolean isCheckSumEnabled() {
        return isCheckSumEnabled;
    }

    /**
     * Get the message configuration.
     *
     * @return the message configuration
     */
    public FixFieldListConfig messageConfig() {
        return messageConfig;
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
     * A FIX field list decoder configuration builder.
     */
    public static class Builder {

        private FixVersion version;
        private boolean isCheckSumEnabled;
        private FixFieldListConfig messageConfig;

        private Builder() {
            version = DEFAULT_VERSION;
            isCheckSumEnabled = DEFAULT_CHECK_SUM_ENABLED;
            messageConfig = DEFAULT_MESSAGE_CONFIG;
        }

        /**
         * Set the FIX protocol version.
         *
         * @param version the FIX protocol version or {@code null} to disable
         *     the BeginString(8) check
         * @return this instance
         */
        public Builder setVersion(final FixVersion version) {
            this.version = version;

            return this;
        }

        /**
         * Set the CheckSum(10) checking policy.
         *
         * @param isCheckSumEnabled true if CheckSum(10) check is enabled,
         *     otherwise false
         * @return this instance
         */
        public Builder setCheckSumEnabled(final boolean isCheckSumEnabled) {
            this.isCheckSumEnabled = isCheckSumEnabled;

            return this;
        }

        /**
         * Set the message configuration.
         *
         * @param messageConfig the message configuration
         * @return this instance
         */
        public Builder setPoolConfig(final FixFieldListConfig messageConfig) {
            this.messageConfig = messageConfig;

            return this;
        }

        /**
         * Build a configuration.
         *
         * @return a configuration
         */
        public FixFieldListDecoderConfig build() {
            return new FixFieldListDecoderConfig(version, isCheckSumEnabled, messageConfig);
        }

    }

}
