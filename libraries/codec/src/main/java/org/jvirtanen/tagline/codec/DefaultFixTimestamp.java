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
package org.jvirtanen.tagline.codec;

import static java.util.Calendar.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * The default implementation of a FIX timestamp.
 */
public class DefaultFixTimestamp implements FixTimestamp {

    private final Calendar calendar;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int milli;

    /**
     * Construct a new instance.
     */
    public DefaultFixTimestamp() {
        this(1970, 1, 1, 0, 0, 0, 0);
    }

    /**
     * Construct a new instance.
     *
     * @param year the year
     * @param month the month
     * @param day the day
     * @throws IllegalArgumentException if the year is less than 0 or greater
     *     than 9999, the month is less than 1 or greater then 12, or if the day
     *     is less than 1 or greater than 31
     */
    public DefaultFixTimestamp(final int year, final int month, final int day) {
        this(year, month, day, 0, 0, 0, 0);
    }

    /**
     * Construct a new instance.
     *
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @param milli the millisecond
     * @throws IllegalArgumentException if the hour is less than 0 or greater
     *     than 23, the minute is less than 0 or greater than 59, the second is
     *     less than 0 or greater than 60, or if the millisecond is less than 0
     *     or greater than 999
     */
    public DefaultFixTimestamp(final int hour, final int minute, final int second,
            final int milli) {
        this(1970, 1, 1, hour, minute, second, milli);
    }

    /**
     * Construct a new instance.
     *
     * @param year the year
     * @param month the month
     * @param day the day
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     * @param milli the millisecond
     * @throws IllegalArgumentException if the year is less than 0 or greater
     *     than 9999, the month is less than 1 or greater than 12, the day is
     *     less than 1 or greater than 31, the hour is less than 0 or greater
     *     than 23, the minute is less than 0 or greater than 59, the second
     *     is less than 0 or greater than 60, or if the millisecond is less than
     *     0 or greater than 999
     */
    public DefaultFixTimestamp(final int year, final int month, final int day,
            final int hour, final int minute, final int second, final int milli) {
        calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        setSecond(second);
        setMilli(milli);
    }

    @Override
    public int year() {
        return year;
    }

    @Override
    public DefaultFixTimestamp setYear(final int year) {
        if (year < 0 || year > 9999)
            notYear(year);

        this.year = year;

        return this;
    }

    @Override
    public int month() {
        return month;
    }

    @Override
    public DefaultFixTimestamp setMonth(final int month) {
        if (month < 1 || month > 12)
            notMonth(month);

        this.month = month;

        return this;
    }

    @Override
    public int day() {
        return day;
    }

    @Override
    public DefaultFixTimestamp setDay(final int day) {
        if (day < 1 || day > 31)
            notDay(day);

        this.day = day;

        return this;
    }

    @Override
    public int hour() {
        return hour;
    }

    @Override
    public DefaultFixTimestamp setHour(final int hour) {
        if (hour < 0 || hour > 23)
            notHour(hour);

        this.hour = hour;

        return this;
    }

    @Override
    public int minute() {
        return minute;
    }

    @Override
    public DefaultFixTimestamp setMinute(final int minute) {
        if (minute < 0 || minute > 59)
            notMinute(minute);

        this.minute = minute;

        return this;
    }

    @Override
    public int second() {
        return second;
    }

    @Override
    public DefaultFixTimestamp setSecond(final int second) {
        if (second < 0 || second > 60)
            notSecond(second);

        this.second = second;

        return this;
    }

    @Override
    public int milli() {
        return milli;
    }

    @Override
    public DefaultFixTimestamp setMilli(final int milli) {
        if (milli < 0 || milli > 999)
            notMilli(milli);

        this.milli = milli;

        return this;
    }

    /**
     * Get the number of milliseconds from the epoch of
     * 1970-01-01T00:00:00.000Z.
     *
     * @return the number of milliseconds from the epoch
     */
    public long getEpochMilli() {
        int adjustedSecond = second;
        int adjustedMilli = milli;

        if (second == 60) {
            adjustedSecond = 59;
            adjustedMilli = 999;
        }

        calendar.set(MILLISECOND, adjustedMilli);
        calendar.set(year, month - 1, day, hour, minute, adjustedSecond);

        return calendar.getTimeInMillis();
    }

    /**
     * Set the number of milliseconds from the epoch of
     * 1970-01-01T00:00:00.000Z.
     *
     * @param epochMilli the number of milliseconds from the epoch
     */
    public void setEpochMilli(final long epochMilli) {
        calendar.setTimeInMillis(epochMilli);

        year = calendar.get(YEAR);
        month = calendar.get(MONTH) + 1;
        day = calendar.get(DAY_OF_MONTH);
        hour = calendar.get(HOUR_OF_DAY);
        minute = calendar.get(MINUTE);
        second = calendar.get(SECOND);
        milli = calendar.get(MILLISECOND);
    }

    /**
     * Get a string representation of this instance.
     *
     * @return a string representation of this instance
     */
    public String toString() {
        return String.format("%04d%02d%02d-%02d:%02d:%02d.%03d",
                year, month, day, hour, minute, second, milli);
    }

    private static void notYear(final int year) {
        throw new IllegalArgumentException("Not a valid year: " + year);
    }

    private static void notMonth(final int month) {
        throw new IllegalArgumentException("Not a valid month: " + month);
    }

    private static void notDay(final int day) {
        throw new IllegalArgumentException("Not a valid day: " + day);
    }

    private static void notHour(final int hour) {
        throw new IllegalArgumentException("Not a valid hour: " + hour);
    }

    private static void notMinute(final int minute) {
        throw new IllegalArgumentException("Not a valid minute: " + minute);
    }

    private static void notSecond(final int second) {
        throw new IllegalArgumentException("Not a valid second: " + second);
    }

    private static void notMilli(final int milli) {
        throw new IllegalArgumentException("Not a valid millisecond: " + milli);
    }

}
