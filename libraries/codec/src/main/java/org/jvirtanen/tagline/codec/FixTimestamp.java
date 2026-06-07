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
