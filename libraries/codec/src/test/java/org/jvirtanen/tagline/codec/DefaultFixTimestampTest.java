/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class DefaultFixTimestampTest {

    @Test
    void defaults() {
        var timestamp = new DefaultFixTimestamp();

        assertEquals(1970, timestamp.year());
        assertEquals(1, timestamp.month());
        assertEquals(1, timestamp.day());
        assertEquals(0, timestamp.hour());
        assertEquals(0, timestamp.minute());
        assertEquals(0, timestamp.second());
        assertEquals(0, timestamp.milli());
    }

    @Test
    void date() {
        var timestamp = new DefaultFixTimestamp(2024, 7, 15);

        assertEquals(2024, timestamp.year());
        assertEquals(7, timestamp.month());
        assertEquals(15, timestamp.day());
        assertEquals(0, timestamp.hour());
        assertEquals(0, timestamp.minute());
        assertEquals(0, timestamp.second());
        assertEquals(0, timestamp.milli());
    }

    @Test
    void time() {
        var timestamp = new DefaultFixTimestamp(22, 10, 36, 250);

        assertEquals(1970, timestamp.year());
        assertEquals(1, timestamp.month());
        assertEquals(1, timestamp.day());
        assertEquals(22, timestamp.hour());
        assertEquals(10, timestamp.minute());
        assertEquals(36, timestamp.second());
        assertEquals(250, timestamp.milli());
    }

    @Test
    void timestamp() {
        var timestamp = new DefaultFixTimestamp(2024, 5, 26, 22, 10, 36, 250);

        assertEquals(2024, timestamp.year());
        assertEquals(5, timestamp.month());
        assertEquals(26, timestamp.day());
        assertEquals(22, timestamp.hour());
        assertEquals(10, timestamp.minute());
        assertEquals(36, timestamp.second());
        assertEquals(250, timestamp.milli());
    }

    @Test
    void minYear() {
        of().setYear(0);
    }

    @Test
    void maxYear() {
        of().setYear(9999);
    }

    @Test
    void minMonth() {
        of().setMonth(1);
    }

    @Test
    void maxMonth() {
        of().setMonth(12);
    }

    @Test
    void minDay() {
        of().setDay(1);
    }

    @Test
    void maxDay() {
        of().setDay(31);
    }

    @Test
    void minHour() {
        of().setHour(0);
    }

    @Test
    void maxHour() {
        of().setHour(23);
    }

    @Test
    void minMinute() {
        of().setMinute(0);
    }

    @Test
    void maxMinute() {
        of().setMinute(59);
    }

    @Test
    void minSecond() {
        of().setSecond(0);
    }

    @Test
    void maxSecond() {
        of().setSecond(60);
    }

    @Test
    void minMilli() {
        of().setMilli(0);
    }

    @Test
    void maxMilli() {
        of().setMilli(999);
    }

    @Test
    void tooLowYear() {
        assertNotValid("Not a valid year: -1", () -> of().setYear(-1));
    }

    @Test
    void tooHighYear() {
        assertNotValid("Not a valid year: 10000", () -> of().setYear(10000));
    }

    @Test
    void tooLowMonth() {
        assertNotValid("Not a valid month: 0", () -> of().setMonth(0));
    }

    @Test
    void tooHighMonth() {
        assertNotValid("Not a valid month: 13", () -> of().setMonth(13));
    }

    @Test
    void tooLowDay() {
        assertNotValid("Not a valid day: 0", () -> of().setDay(0));
    }

    @Test
    void tooHighDay() {
        assertNotValid("Not a valid day: 32", () -> of().setDay(32));
    }

    @Test
    void tooLowHour() {
        assertNotValid("Not a valid hour: -1", () -> of().setHour(-1));
    }

    @Test
    void tooHighHour() {
        assertNotValid("Not a valid hour: 24", () -> of().setHour(24));
    }

    @Test
    void tooLowMinute() {
        assertNotValid("Not a valid minute: -1", () -> of().setMinute(-1));
    }

    @Test
    void tooHighMinute() {
        assertNotValid("Not a valid minute: 60", () -> of().setMinute(60));
    }

    @Test
    void tooLowSecond() {
        assertNotValid("Not a valid second: -1", () -> of().setSecond(-1));
    }

    @Test
    void tooHighSecond() {
        assertNotValid("Not a valid second: 61", () -> of().setSecond(61));
    }

    @Test
    void tooLowMilli() {
        assertNotValid("Not a valid millisecond: -1", () -> of().setMilli(-1));
    }

    @Test
    void tooHighMilli() {
        assertNotValid("Not a valid millisecond: 1000", () -> of().setMilli(1000));
    }

    @Test
    void defaultEpochMilli() {
        assertEquals(0, of().getEpochMilli());
    }

    @Test
    void timestampEpochMilli() {
        assertEquals(1716761436250l, of(2024, 5, 26, 22, 10, 36, 250).getEpochMilli());
    }

    @Test
    void leapSecond() {
        var leapSecond = of(2016, 12, 31, 23, 59, 60, 999);
        var nextTimestamp = of(2017, 1, 1, 0, 0, 0, 0);

        assertTrue(nextTimestamp.getEpochMilli() > leapSecond.getEpochMilli());
    }

    private static DefaultFixTimestamp of() {
        return new DefaultFixTimestamp();
    }

    private static DefaultFixTimestamp of(final int year, final int month, final int day,
            final int hour, final int minute, final int second, final int milli) {
        return new DefaultFixTimestamp(year, month, day, hour, minute, second, milli);
    }

    private static void assertNotValid(final String message, final Executable executable) {
        var exception = assertThrows(IllegalArgumentException.class, executable);

        assertEquals(message, exception.getMessage());
    }

}
