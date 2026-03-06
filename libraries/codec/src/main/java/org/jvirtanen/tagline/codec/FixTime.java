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
 * A FIX time.
 */
public interface FixTime {

    /**
     * Get the hour.
     *
     * @return the hour
     */
    int hour();

    /**
     * Set the hour.
     *
     * @param hour the hour
     * @return this instance
     * @throws IllegalArgumentException if the hour is less than 0 or greater
     *     than 23
     */
    FixTime setHour(int hour);

    /**
     * Get the minute.
     *
     * @return the minute
     */
    int minute();

    /**
     * Set the minute.
     *
     * @param minute the minute
     * @return this instance
     * @throws IllegalArgumentException if the minute is less than 0 or greater
     *     than 59
     */
    FixTime setMinute(int minute);

    /**
     * Get the second.
     *
     * @return the second
     */
    int second();

    /**
     * Set the second.
     *
     * @param second the second
     * @return this instance
     * @throws IllegalArgumentException if the second is less than 0 or greater
     *     than 60
     */
    FixTime setSecond(int second);

    /**
     * Get the millisecond.
     *
     * @return the millisecond
     */
    int milli();

    /**
     * Set the millisecond.
     *
     * @param milli the millisecond
     * @return this instance
     * @throws IllegalArgumentException if the millisecond is less than 0 or
     *     greater than 999
     */
    FixTime setMilli(int milli);

}
