/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * A FIX timestamp.
 */
public interface FixTimestamp extends FixDate, FixTime {

    @Override
    FixTimestamp setYear(int year);

    @Override
    FixTimestamp setMonth(int month);

    @Override
    FixTimestamp setDay(int day);

    @Override
    FixTimestamp setHour(int hour);

    @Override
    FixTimestamp setMinute(int minute);

    @Override
    FixTimestamp setSecond(int second);

    @Override
    FixTimestamp setMilli(int milli);

}
