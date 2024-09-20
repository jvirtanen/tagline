/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class FixFieldIteratorTest {

    @Test
    void empty() {
        var fields = iterate("");

        assertFalse(fields.hasNext());
        assertNoSuchElement(fields);
    }

    @Test
    void one() {
        var fields = iterate("35=D\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(35, field.tag());
        assertTrue("D".contentEquals(field.asString()));

        assertFalse(fields.hasNext());
        assertNoSuchElement(fields);
    }

    @Test
    void two() {
        var fields = iterate("35=D\u000149=initiator\u0001");

        assertTrue(fields.hasNext());

        var firstField = fields.next();

        assertEquals(35, firstField.tag());
        assertTrue("D".contentEquals(firstField.asString()));

        assertTrue(fields.hasNext());

        var secondField = fields.next();

        assertEquals(49, secondField.tag());
        assertTrue("initiator".contentEquals(secondField.asString()));

        assertFalse(fields.hasNext());
        assertNoSuchElement(fields);
    }

    @Test
    void message() {
        var fields = iterate(FIX_4_2, "8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001", 14, 5, 181);

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(35, field.tag());
        assertTrue("D".contentEquals(field.asString()));

        assertFalse(fields.hasNext());
        assertNoSuchElement(fields);
    }

    @Test
    void asBoolean() {
        var fields = iterate("141=Y\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(141, field.tag());
        assertEquals(1, field.length());
        assertTrue(field.asBoolean());
    }

    @Test
    void asChar() {
        var fields = iterate("35=D\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(35, field.tag());
        assertEquals(1, field.length());
        assertEquals('D', field.asChar());
    }

    @Test
    void asInt() {
        var fields = iterate("38=100\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(38, field.tag());
        assertEquals(3, field.length());
        assertEquals(100, field.asInt());
    }

    @Test
    void asFloat() {
        var fields = iterate("44=150.00\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(44, field.tag());
        assertEquals(6, field.length());
        assertEquals(150.00, field.asFloat());
    }

    @Test
    void asString() {
        var fields = iterate("55=FOO\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(55, field.tag());
        assertEquals(3, field.length());
        assertTrue("FOO".contentEquals(field.asString()));
    }

    @Test
    void asDate() {
        var fields = iterate("272=20240107\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();
        var container = new DefaultFixTimestamp();

        field.asDate(container);

        assertEquals(272, field.tag());
        assertEquals(8, field.length());
        assertEquals(2024, container.year());
        assertEquals(1, container.month());
        assertEquals(7, container.day());
    }

    @Test
    void asBytes() {
        var fields = iterate("55=FOO\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals(55, field.tag());
        assertArrayEquals(new byte[] { 'F', 'O', 'O', }, Arrays.copyOf(field.asBytes(), field.length()));
    }

    @Test
    void charAt() {
        var fields = iterate("55=FOO\u0001");

        assertTrue(fields.hasNext());

        var field = fields.next();

        assertEquals('O', field.charAt(2));
    }

    @Test
    void charAtWithTooSmallIndex() {
        var field = iterate("55=FOO\u0001").next();

        assertThrows(IndexOutOfBoundsException.class, () -> field.charAt(-1));
    }

    @Test
    void charAtWithTooLargeIndex() {
        var field = iterate("55=FOO\u0001").next();

        assertThrows(IndexOutOfBoundsException.class, () -> field.charAt(3));
    }

    @Test
    void incompleteTag() {
        var fields = iterate("35");

        assertIncompleteField(fields);
    }

    @Test
    void incompleteValue() {
        var fields = iterate("35=D");

        assertIncompleteField(fields);
    }

    private static FixFieldIterator iterate(final String bytes) {
        var buffer = copiedBuffer(bytes);

        return new FixFieldIterator().iterate(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    private static FixFieldIterator iterate(final FixVersion version, final String bytes,
            final int bodyOffset, final int bodyLength, final int checkSum) {
        var content = copiedBuffer(bytes);
        var message = new DefaultInboundFixMessage(version, content, bodyOffset, bodyLength, checkSum);

        return new FixFieldIterator().iterate(message);
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

    private static void assertDecoderError(final String message, final Executable executable) {
        var exception = assertThrows(FixDecoderException.class, executable);

        assertEquals(message, exception.getMessage());
    }

    private static void assertIncompleteField(final FixFieldIterator iterator) {
        assertDecoderError("Incomplete field", () -> iterator.next());
    }

    private static void assertNoSuchElement(final FixFieldIterator iterator) {
        var exception = assertThrows(NoSuchElementException.class, () -> iterator.next());

        assertNull(exception.getMessage());
    }

}
