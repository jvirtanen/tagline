/*
 * Copyright 2024 Jussi Virtanen
 */

/**
 * <p>A fast Financial Information Exchange (FIX) codec for Netty.</p>
 *
 * <p>Add an {@link OutboundFixMessageEncoder} to a channel to encode outgoing
 * FIX messages and an {@link InboundFixMessageDecoder} as well as a {@link
 * FixFieldListDecoder} to decode incoming FIX messages. The handlers take care
 * of the BeginString(8), BodyLength(9), and CheckSum(10) fields. Use the
 * {@link OutboundFixMessage}, {@link FixFieldList}, and {@link FixValue}
 * methods, such as {@link OutboundFixMessage#addInt(int,long)
 * addInt(int,long)} or {@link FixValue#asInt() asInt()}, to handle the
 * rest.</p>
 */
package org.jvirtanen.tagline.codec;
