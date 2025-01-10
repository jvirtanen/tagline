/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.handler.codec.CorruptedFrameException;

/**
 * Indicates a garbled FIX message.
 */
public class GarbledFixMessageException extends CorruptedFrameException {

    /**
     * Construct a new instance.
     *
     * @param message the message
     */
    public GarbledFixMessageException(final String message) {
        super(message);
    }

    /**
     * Construct a new instance.
     *
     * @param cause the cause
     */
    public GarbledFixMessageException(final Throwable cause) {
        super(cause);
    }

}
