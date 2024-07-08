/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;

import java.util.Arrays;

/**
 * The FIX protocol version.
 */
public class FixVersion {

    /**
     * FIX 4.2.
     */
    public static final FixVersion FIX_4_2 = new FixVersion("FIX.4.2");

    /**
     * FIX 4.3.
     */
    public static final FixVersion FIX_4_3 = new FixVersion("FIX.4.3");

    /**
     * FIX 4.4.
     */
    public static final FixVersion FIX_4_4 = new FixVersion("FIX.4.4");

    /**
     * FIXT 1.1.
     */
    public static final FixVersion FIXT_1_1 = new FixVersion("FIXT.1.1");

    private final String beginString;

    private final int length;
    private final byte[] bytes;
    private final long bits;

    /**
     * Construct a new instance.
     *
     * @param beginString the BeginString(8) value
     */
    public FixVersion(final String beginString) {
        this(beginString, getBytes(beginString));
    }

    private FixVersion(final byte[] bytes) {
        this(getBeginString(bytes), bytes);
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

    static FixVersion fromBytes(final byte[] bytes, final int length) {
        return new FixVersion(Arrays.copyOf(bytes, length));
    }

    int length() {
        return length;
    }

    byte[] bytes() {
        return bytes;
    }

    long bits() {
        return bits;
    }

    boolean equals(final byte[] bytes, final int length) {
        return Arrays.equals(this.bytes, 0, this.length, bytes, 0, length);
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
