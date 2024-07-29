/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import io.netty.handler.codec.TooLongFrameException;

/**
 * Indicates a too long inbound FIX message.
 */
public class TooLongInboundFixMessageException extends TooLongFrameException {

    /**
     * Construct a new instance.
     */
    public TooLongInboundFixMessageException() {
    }

}
