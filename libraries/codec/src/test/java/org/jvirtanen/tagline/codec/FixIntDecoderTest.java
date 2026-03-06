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

import static java.nio.charset.StandardCharsets.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class FixIntDecoderTest {

    @Test
    void zero() {
        assertEquals(0, decode("0"));
    }

    @Test
    void one() {
        assertEquals(1, decode("1"));
    }

    @Test
    void twelve() {
        assertEquals(12, decode("12"));
    }

    @Test
    void minusZero() {
        assertEquals(0, decode("-0"));
    }

    @Test
    void minusOne() {
        assertEquals(-1, decode("-1"));
    }

    @Test
    void minusTwelve() {
        assertEquals(-12, decode("-12"));
    }

    @Test
    void minValue() {
        assertEquals(Long.MIN_VALUE, decode(Long.toString(Long.MIN_VALUE)));
    }

    @Test
    void maxValue() {
        assertEquals(Long.MAX_VALUE, decode(Long.toString(Long.MAX_VALUE)));
    }

    @Test
    void positiveLeadingZeroes() {
        assertEquals(123, decode("00123"));
    }

    @Test
    void negativeLeadingZeroes() {
        assertEquals(-123, decode("-00123"));
    }

    @Test
    void negativeAdditionOverflow() {
        var value = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

        assertNotInt("Too small Int", value.toString());
    }

    @Test
    void negativeMultiplicationOverflow() {
        var value = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.TEN);

        assertNotInt("Too small Int", value.toString());
    }

    @Test
    void negativeLengthOverflow() {
        var value = BigInteger.valueOf(Long.MIN_VALUE).multiply(BigInteger.TEN);

        assertNotInt("Too small Int", value.toString());
    }

    @Test
    void positiveAdditionOverflow() {
        var value = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        assertNotInt("Too large Int", value.toString());
    }

    @Test
    void positiveMultiplicationOverflow() {
        var value = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.TEN);

        assertNotInt("Too large Int", value.toString());
    }

    @Test
    void positiveLengthOverflow() {
        var value = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.TEN);

        assertNotInt("Too large Int", value.toString());
    }

    @Test
    void empty() {
        assertNotInt("Not an Int", "");
    }

    @Test
    void minus() {
        assertNotInt("Not an Int", "-");
    }

    @Test
    void invalidByte() {
        assertNotInt("Not an Int", "Y");
    }

    private static long decode(final String value) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);

        return FixIntDecoder.decode(bytes, 1, bytes.length - 2);
    }

    private static void assertNotInt(final String message, final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals(message, exception.getMessage());
    }

}
