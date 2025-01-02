/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.handler.codec.TooLongFrameException;

/**
 * Indicates a too long FIX message.
 */
public class TooLongFixMessageException extends TooLongFrameException {

    /**
     * Construct a new instance.
     */
    public TooLongFixMessageException() {
    }

}
