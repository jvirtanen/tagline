/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.handler.codec.DecoderException;

/**
 * Indicates a FIX decoder error.
 */
public class FixDecoderException extends DecoderException {

    /**
     * Construct a new instance.
     */
    public FixDecoderException() {
    }

    /**
     * Construct a new instance.
     *
     * @param message the detail message
     */
    public FixDecoderException(final String message) {
        super(message);
    }

    /**
     * Construct a new instance.
     *
     * @param cause the cause
     */
    public FixDecoderException(final Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new instance.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public FixDecoderException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
