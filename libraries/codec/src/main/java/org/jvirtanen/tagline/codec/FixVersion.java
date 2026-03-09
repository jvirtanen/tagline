/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import io.netty.buffer.ByteBuf;

/**
 * The FIX protocol version.
 */
public class FixVersion {

    /**
     * FIX 4.2.
     */
    public static final FixVersion FIX_4_2 = FixVersion.of("FIX.4.2");

    /**
     * FIX 4.3.
     */
    public static final FixVersion FIX_4_3 = FixVersion.of("FIX.4.3");

    /**
     * FIX 4.4.
     */
    public static final FixVersion FIX_4_4 = FixVersion.of("FIX.4.4");

    /**
     * FIXT 1.1.
     */
    public static final FixVersion FIXT_1_1 = FixVersion.of("FIXT.1.1");

    private final String beginString;

    private final int length;
    private final byte[] bytes;
    private final long bits;

    /**
     * Construct a new instance.
     *
     * @param beginString the BeginString(8) value
     * @return a new instance
     */
    public static FixVersion of(final String beginString) {
        return new FixVersion(beginString, getBytes(beginString));
    }

    static FixVersion of(final ByteBuf buffer, final int offset, final int length) {
        var bytes = new byte[length];

        buffer.getBytes(offset, bytes);

        return new FixVersion(getBeginString(bytes), bytes);
    }

    private FixVersion(final String beginString, final byte[] bytes) {
        this.beginString = beginString;

        this.length = bytes.length;
        this.bytes = bytes;
        this.bits = getBits(bytes);
    }

    /**
     * Get the BeginString(8) value.
     *
     * @return the BeginString(8) value
     */
    public String beginString() {
        return beginString;
    }

    /**
     * Returns true if the specified object is equal to this instance,
     * otherwise returns false.
     *
     * @return true if the specified object is equal to this instance,
     *     otherwise false
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof FixVersion) {
            var v = (FixVersion)obj;

            return beginString.equals(v.beginString);
        }

        return false;
    }

    /**
     * Get the hash code for this instance.
     *
     * @return the hash code for this instance
     */
    @Override
    public int hashCode() {
        return beginString.hashCode();
    }

    /**
     * Get a string representation of this instance.
     *
     * @return a string representation of this instance
     */
    @Override
    public String toString() {
        return beginString;
    }

    int length() {
        return length;
    }

    void encode(final ByteBuf buffer) {
        if (bits != 0)
            buffer.writeLong(bits);
        else
            buffer.writeBytes(bytes);
    }

    boolean matches(final ByteBuf buffer, final int offset, final int length) {
        if (length < this.length)
            return false;

        if (bits != 0)
            return bitsMatch(buffer, offset);
        else
            return bytesMatch(buffer, offset);
    }

    private boolean bitsMatch(final ByteBuf buffer, final int offset) {
        return buffer.getLong(offset) == bits;
    }

    private boolean bytesMatch(final ByteBuf buffer, final int offset) {
        for (int i = 0; i < length; i++) {
            if (buffer.getByte(offset + i) != bytes[i])
                return false;
        }

        return true;
    }

    private static String getBeginString(final byte[] bytes) {
        return new String(bytes, 0, bytes.length - 1, ISO_8859_1);
    }

    private static byte[] getBytes(final String beginString) {
        return String.format("%s\u0001", beginString).getBytes(ISO_8859_1);
    }

    private static long getBits(final byte[] bytes) {
        if (bytes.length != 8)
            return 0;

        long bits = 0;

        for (int i = 0; i < 8; i++) {
            bits <<= 8;
            bits |= bytes[i];
        }

        return bits;
    }

}
