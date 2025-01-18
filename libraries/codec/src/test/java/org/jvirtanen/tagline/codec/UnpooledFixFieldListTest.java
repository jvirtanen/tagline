/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.jvirtanen.tagline.codec.FixVersion.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

class UnpooledFixFieldListTest {

    @Test
    void empty() {
        var fields = decode("");

        assertEquals(0, fields.size());
    }

    @Test
    void one() {
        var fields = decode("35=D\u0001");

        assertEquals(1, fields.size());

        var value = fields.valueAt(0);

        assertEquals(35, fields.tagAt(0));
        assertEquals(1, value.length());
        assertTrue("D".contentEquals(value));
    }

    @Test
    void two() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertEquals(2, fields.size());

        var firstValue = fields.valueAt(0);

        assertEquals(35, fields.tagAt(0));
        assertEquals(1, firstValue.length());
        assertTrue("D".contentEquals(firstValue));

        var secondValue = fields.valueAt(1);

        assertEquals(49, fields.tagAt(1));
        assertEquals(9, secondValue.length());
        assertTrue("initiator".contentEquals(secondValue));
    }

    @Test
    void message() {
        var fields = decode(FIX_4_2, "8=FIX.4.2\u00019=5\u000135=D\u000110=181\u0001", 14, 5, 181);

        assertEquals(4, fields.size());

        var firstValue = fields.valueAt(0);

        assertEquals(8, fields.tagAt(0));
        assertEquals(7, firstValue.length());
        assertTrue("FIX.4.2".contentEquals(firstValue));

        var secondValue = fields.valueAt(1);

        assertEquals(9, fields.tagAt(1));
        assertEquals(1, secondValue.length());
        assertTrue("5".contentEquals(secondValue));

        var thirdValue = fields.valueAt(2);

        assertEquals(35, fields.tagAt(2));
        assertEquals(1, thirdValue.length());
        assertTrue("D".contentEquals(thirdValue));

        var fourthValue = fields.valueAt(3);

        assertEquals(10, fields.tagAt(3));
        assertEquals(3, fourthValue.length());
        assertTrue("181".contentEquals(fourthValue));
    }

    @Test
    void clear() {
        var fields = decode("35=D\u000149=initiator\u0001");
        var value = fields.valueAt(0);

        fields.clear();

        assertEquals(0, fields.size());
        assertEquals(0, value.length());
    }

    @Test
    void tagAtWithTooSmallIndex() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertThrows(IndexOutOfBoundsException.class, () -> fields.tagAt(-1));
    }

    @Test
    void tagAtWithTooLargeIndex() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertThrows(IndexOutOfBoundsException.class, () -> fields.tagAt(2));
    }

    @Test
    void valueAtWithTooSmallIndex() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertThrows(IndexOutOfBoundsException.class, () -> fields.valueAt(-1));
    }

    @Test
    void valueAtWithTooLargeIndex() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertThrows(IndexOutOfBoundsException.class, () -> fields.valueAt(2));
    }

    @Test
    void indexOfFirst() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertEquals(1, fields.indexOf(49));
    }

    @Test
    void indexOfFirstWithNoOccurrence() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertEquals(-1, fields.indexOf(56));
    }

    @Test
    void indexOfNext() {
        var fields = decode("268=2\u0001270=100.0\u0001271=50\u0001270=125.0\u0001271=100\u0001");

        assertEquals(3, fields.indexOf(270, 2));
    }

    @Test
    void indexOfNextWithNoMoreOccurrences() {
        var fields = decode("268=2\u0001270=100.0\u0001271=50\u0001270=125.0\u0001271=100\u0001");

        assertEquals(-1, fields.indexOf(270, 4));
    }

    @Test
    void valueOfFirst() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertTrue("initiator".contentEquals(fields.valueOf(49)));
    }

    @Test
    void valueOfFirstWithNoOccurrence() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertNull(fields.valueOf(56));
    }

    @Test
    void valueOfNext() {
        var fields = decode("268=2\u0001270=100.0\u0001271=50\u0001270=125.0\u0001271=100\u0001");

        assertTrue("125.0".contentEquals(fields.valueOf(270, 2)));
    }

    @Test
    void valueOfNextWithNoMoreOccurrences() {
        var fields = decode("268=2\u0001270=100.0\u0001271=50\u0001270=125.0\u0001271=100\u0001");

        assertNull(fields.valueOf(270, 4));
    }

    @Test
    void string() {
        var fields = decode("35=D\u000149=initiator\u0001");

        assertEquals("35=D|49=initiator|", fields.toString());
    }

    @Test
    void enlargeBuffer() {
        var config = FixFieldListConfig.newBuilder()
            .setInitialBufferSize(1)
            .build();

        decode("35=D\u000149=initiator\u0001", config);
    }

    @Test
    void increaseCapacity() {
        var config = FixFieldListConfig.newBuilder()
            .setInitialCapacity(1)
            .build();

        decode("35=D\u000149=initiator\u0001", config);
    }

    @Test
    void enlargeBufferAndIncreaseCapacity() {
        var config = FixFieldListConfig.newBuilder()
            .setInitialCapacity(1)
            .setInitialBufferSize(1)
            .build();

        decode("35=D\u000149=initiator\u0001", config);
    }

    @Test
    void incompleteTag() {
        assertIncompleteField("35");
    }

    @Test
    void incompleteValue() {
        assertIncompleteField("35=D");
    }

    @Test
    void valueByteAt() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertEquals('O', value.byteAt(2));
    }

    @Test
    void valueCharAt() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertEquals('O', value.charAt(2));
    }

    @Test
    void valueCharAtWithTooSmallIndex() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertThrows(IndexOutOfBoundsException.class, () -> value.charAt(-1));
    }

    @Test
    void valueCharAtWithTooLargeIndex() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertThrows(IndexOutOfBoundsException.class, () -> value.charAt(3));
    }

    @Test
    void valueAsBoolean() {
        var value = decode("141=Y\u0001").valueAt(0);

        assertEquals(true, value.asBoolean());
    }

    @Test
    void valueAsChar() {
        var value = decode("35=D\u0001").valueAt(0);

        assertEquals('D', value.asChar());
    }

    @Test
    void valueAsInt() {
        var value = decode("38=100\u0001").valueAt(0);

        assertEquals(100, value.asInt());
    }

    @Test
    void valueAsFloat() {
        var value = decode("44=150.00\u0001").valueAt(0);

        assertEquals(150.00, value.asFloat());
    }

    @Test
    void valueAsString() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertTrue("FOO".contentEquals(value.asString()));
    }

    @Test
    void valueAsDate() {
        var value = decode("272=20240107\u0001").valueAt(0);

        var container = new DefaultFixTimestamp();

        value.asDate(container);

        assertEquals(2024, container.year());
        assertEquals(1, container.month());
        assertEquals(7, container.day());
    }

    @Test
    void valueAsTime() {
        var value = decode("273=16:44:30.950\u0001").valueAt(0);

        var container = new DefaultFixTimestamp();

        value.asTime(container);

        assertEquals(16, container.hour());
        assertEquals(44, container.minute());
        assertEquals(30, container.second());
        assertEquals(950, container.milli());
    }

    @Test
    void valueAsTimestampObject() {
        var value = decode("52=20240107-16:44:30.950\u0001").valueAt(0);

        var container = new DefaultFixTimestamp();

        value.asTimestamp(container);

        assertEquals(2024, container.year());
        assertEquals(1, container.month());
        assertEquals(7, container.day());
        assertEquals(16, container.hour());
        assertEquals(44, container.minute());
        assertEquals(30, container.second());
        assertEquals(950, container.milli());
    }

    @Test
    void valueAsTimestampLong() {
        var value = decode("52=20240107-16:44:30.950\u0001").valueAt(0);

        assertEquals(1704645870950l, value.asTimestamp());
    }

    @Test
    void valueAsBytes() {
        var value = decode("55=FOO\u0001").valueAt(0);
        var bytes = new byte[value.length()];

        value.asBytes(bytes);

        assertArrayEquals(new byte[] { 'F', 'O', 'O', }, bytes);
    }

    @Test
    void valueAsBytesWithTooSmallArray() {
        var value = decode("55=FOO\u0001").valueAt(0);
        var bytes = new byte[2];

        assertThrows(IndexOutOfBoundsException.class, () -> value.asBytes(bytes));
    }

    @Test
    void valueSubSequence() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertTrue("OO".contentEquals(value.subSequence(1, 3)));
    }

    @Test
    void emptyValueSubSequence() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertTrue("".contentEquals(value.subSequence(2, 2)));
    }

    @Test
    void valueSubSequenceWithTooSmallStart() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertThrows(IndexOutOfBoundsException.class, () -> value.subSequence(-1, 3));
    }

    @Test
    void valueSubSequenceWithTooLargeStart() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertThrows(IndexOutOfBoundsException.class, () -> value.subSequence(3, 2));
    }

    @Test
    void valueSubSequenceWithTooLargeEnd() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertThrows(IndexOutOfBoundsException.class, () -> value.subSequence(0, 4));
    }

    @Test
    void valueToString() {
        var value = decode("55=FOO\u0001").valueAt(0);

        assertEquals("FOO", value.toString());
    }

    private static FixFieldList decode(final String bytes) {
        return decode(bytes, FixFieldListConfig.DEFAULTS);
    }

    private static UnpooledFixFieldList decode(final String bytes, final FixFieldListConfig config) {
        var fields = new UnpooledFixFieldList(config);

        fields.decode(copiedBuffer(bytes));

        return fields;
    }

    private static UnpooledFixFieldList decode(final FixVersion version, final String bytes,
            final int bodyOffset, final int bodyLength, final int checkSum) {
        var content = copiedBuffer(bytes);
        var message = new DefaultInboundFixMessage(version, content, bodyOffset, bodyLength, checkSum);

        var fields = new UnpooledFixFieldList();

        fields.decode(message);

        return fields;
    }

    private static ByteBuf copiedBuffer(final String bytes) {
        return Unpooled.copiedBuffer(bytes, ISO_8859_1);
    }

    private static void assertIncompleteField(final String bytes) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(bytes));

        assertEquals("Incomplete field", exception.getMessage());
    }

}
