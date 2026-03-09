/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.jvirtanen.tagline.codec.FixConstants.*;

import io.netty.buffer.ByteBuf;

/**
 * The FIX protocol version.
 */
public abstract class FixVersion {

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

    /**
     * Construct a new instance.
     *
     * @param beginString the BeginString(8) value
     * @return a new instance
     */
    public static FixVersion of(final String beginString) {
        var bytes = toBytes(beginString);

        if (bytes.length == 8)
            return new Bits(beginString, toBits(bytes));
        else
            return new Bytes(beginString, bytes);
    }

    static FixVersion of(final ByteBuf buffer, final int offset, final int length) {
        var beginString = buffer.toString(offset, length - 1, ISO_8859_1);

        if (length == 8) {
            long bits = buffer.getLong(offset);

            return new Bits(beginString, bits);
        } else {
            var bytes = new byte[length];

            buffer.getBytes(offset, bytes);

            return new Bytes(beginString, bytes);
        }
    }

    private FixVersion(final String beginString) {
        this.beginString = beginString;
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

    abstract int length();

    abstract void encode(ByteBuf buffer);

    abstract boolean matches(ByteBuf buffer, int offset, int length);

    private static byte[] toBytes(final String beginString) {
        return String.format("%s\u0001", beginString).getBytes(ISO_8859_1);
    }

    private static long toBits(final byte[] bytes) {
        long bits = 0;

        for (int i = 0; i < 8; i++) {
            bits <<= 8;
            bits |= bytes[i];
        }

        return bits;
    }

    private static class Bytes extends FixVersion {

        final byte[] value;

        Bytes(final String beginString, final byte[] value) {
            super(beginString);

            this.value = value;
        }

        @Override
        int length() {
            return value.length;
        }

        @Override
        void encode(final ByteBuf buffer) {
            buffer.writeBytes(value);
        }

        @Override
        boolean matches(final ByteBuf buffer, final int offset, final int length) {
            int limit = value.length;

            if (length < limit)
                return false;

            for (int i = 0; i < limit; i++) {
                if (buffer.getByte(offset + i) != value[i])
                    return false;
            }

            return true;
        }

    }

    private static class Bits extends FixVersion {

        private final long value;

        Bits(final String beginString, final long value) {
            super(beginString);

            this.value = value;
        }

        @Override
        int length() {
            return 8;
        }

        @Override
        void encode(final ByteBuf buffer) {
            buffer.writeLong(value);
        }

        @Override
        boolean matches(final ByteBuf buffer, final int offset, final int length) {
            if (length < 8)
                return false;

            return buffer.getLong(offset) == value;
        }

    }

}
