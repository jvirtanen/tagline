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
