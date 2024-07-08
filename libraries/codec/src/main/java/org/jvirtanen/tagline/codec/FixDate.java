/*
 * Copyright 2024 Jussi Virtanen
 */
package org.jvirtanen.tagline.codec;

/**
 * A FIX date.
 */
public interface FixDate {

    /**
     * Get the year.
     *
     * @return the year
     */
    int year();

    /**
     * Set the year.
     *
     * @param year the year
     * @return this instance
     * @throws IllegalArgumentException if the year is less than 0 or greater
     *     than 9999
     */
    FixDate setYear(int year);

    /**
     * Get the month.
     *
     * @return the month
     */
    int month();

    /**
     * Set the month.
     *
     * @param month the month
     * @return this instance
     * @throws IllegalArgumentException if the month is less than 1 or greater
     *     than 12
     */
    FixDate setMonth(int month);

    /**
     * Get the day.
     *
     * @return the day
     */
    int day();

    /**
     * Set the day.
     *
     * @param day the day
     * @return this instance
     * @throws IllegalArgumentException if the day is less than 1 or greater
     *     than 31
     */
    FixDate setDay(int day);

}
