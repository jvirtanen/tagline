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

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class FixFloatDecoderTest {

    @Test
    void zero() {
        assertEquals(of(0, 0), decode("0"));
    }

    @Test
    void one() {
        assertEquals(of(1, 0), decode("1"));
    }

    @Test
    void onePointZero() {
        assertEquals(of(10, 1), decode("1.0"));
    }

    @Test
    void twelve() {
        assertEquals(of(12, 0), decode("12"));
    }

    @Test
    void twelvePointThree() {
        assertEquals(of(123, 1), decode("12.3"));
    }

    @Test
    void minusZero() {
        assertEquals(of(0, 0), decode("-0"));
    }

    @Test
    void minusOne() {
        assertEquals(of(-1, 0), decode("-1"));
    }

    @Test
    void minusOnePointZero() {
        assertEquals(of(-10, 1), decode("-1.0"));
    }

    @Test
    void minusTwelve() {
        assertEquals(of(-12, 0), decode("-12"));
    }

    @Test
    void minusTwelvePointThree() {
        assertEquals(of(-123, 1), decode("-12.3"));
    }

    @Test
    void minValue() {
        assertEquals(of(Long.MIN_VALUE, 0), decode(Long.toString(Long.MIN_VALUE)));
    }

    @Test
    void maxValue() {
        assertEquals(of(Long.MAX_VALUE, 0), decode(Long.toString(Long.MAX_VALUE)));
    }

    @Test
    void minUnscaledValue() {
        var value = BigDecimal.valueOf(Long.MIN_VALUE).movePointLeft(18);

        assertEquals(of(Long.MIN_VALUE, 18), decode(value.toPlainString()));
    }

    @Test
    void maxUnscaledValue() {
        var value = BigDecimal.valueOf(Long.MAX_VALUE).movePointLeft(18);

        assertEquals(of(Long.MAX_VALUE, 18), decode(value.toPlainString()));
    }

    @Test
    void positiveLeadingZeroes() {
        assertEquals(of(123, 0), decode("00123"));
    }

    @Test
    void negativeLeadingZeroes() {
        assertEquals(of(-123, 0), decode("-00123"));
    }

    @Test
    void negativeUnscaledAdditionOverflow() {
        var value = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

        assertNotFloat("Unrepresentable Float", value.toString());
    }

    @Test
    void negativeUnscaledMultiplicationOverflow() {
        var value = BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.TEN).movePointLeft(9);

        assertNotFloat("Unrepresentable Float", value.toPlainString());
    }

    @Test
    void negativeUnscaledLengthOverflow() {
        var value = BigDecimal.valueOf(Long.MIN_VALUE).multiply(BigDecimal.TEN).movePointLeft(18);

        assertNotFloat("Unrepresentable Float", value.toPlainString());
    }

    @Test
    void positiveUnscaledAdditionOverflow() {
        var value = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        assertNotFloat("Unrepresentable Float", value.toString());
    }

    @Test
    void positiveUnscaledMultiplicationOverflow() {
        var value = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.TEN).movePointLeft(9);

        assertNotFloat("Unrepresentable Float", value.toPlainString());
    }

    @Test
    void positiveUnscaledLengthOverflow() {
        var value = BigDecimal.valueOf(Long.MAX_VALUE).multiply(BigDecimal.TEN).movePointLeft(18);

        assertNotFloat("Unrepresentable Float", value.toPlainString());
    }

    @Test
    void negativeScaleOverflow() {
        var value = BigDecimal.valueOf(Long.MIN_VALUE).movePointLeft(19);

        assertNotFloat("Unrepresentable Float", value.toPlainString());
    }

    @Test
    void positiveScaleOverflow() {
        var value = BigDecimal.valueOf(Long.MAX_VALUE).movePointLeft(19);

        assertNotFloat("Unrepresentable Float", value.toPlainString());
    }

    @Test
    void empty() {
        assertNotFloat("Not a Float", "");
    }

    @Test
    void minus() {
        assertNotFloat("Not a Float", "-");
    }

    @Test
    void point() {
        assertNotFloat("Not a Float", ".");
    }

    @Test
    void precedingPoint() {
        assertNotFloat("Not a Float", ".1");
    }

    @Test
    void trailingPoint() {
        assertNotFloat("Not a Float", "1.");
    }

    @Test
    void minusPoint() {
        assertNotFloat("Not a Float", "-.");
    }

    @Test
    void minusPrecedingPoint() {
        assertNotFloat("Not a Float", "-.1");
    }

    @Test
    void minusTrailingPoint() {
        assertNotFloat("Not a Float", "-1.");
    }

    @Test
    void invalidByte() {
        assertNotFloat("Not a Float", "Y");
    }

    private static FixFloat of(final long unscaledValue, final int scale) {
        return new DefaultFixFloat(unscaledValue, scale);
    }

    private static FixFloat decode(final String value) {
        var bytes = String.format(">%s<", value).getBytes(ISO_8859_1);
        var container = new DefaultFixFloat();

        FixFloatDecoder.decode(bytes, 1, bytes.length - 2, container);

        return container;
    }

    private static void assertNotFloat(final String message, final String value) {
        var exception = assertThrows(FixDecoderException.class, () -> decode(value));

        assertEquals(message, exception.getMessage());
    }

}
