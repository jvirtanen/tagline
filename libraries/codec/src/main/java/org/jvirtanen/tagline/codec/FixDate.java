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
