import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * String Calculator Kata Tests
 * http://osherove.com/tdd-kata-1/
 *
 * An empty string returns zero
 * A single number returns the value
 * Two numbers, comma delimited, returns the sum
 * Two numbers, newline delimited, returns the sum
 * Three numbers, delimited either way, returns the sum
 * Negative numbers throw an exception
 * Numbers greater than 1000 are ignored
 * A single char delimiter can be defined on the first line (e.g. //# for a ‘#’ as the delimiter)
 * A multi char delimiter can be defined on the first line (e.g. //[###] for ‘###’ as the delimiter)
 * Many single or multi-char delimiters can be defined (each wrapped in square brackets)
 *
 */
class StringCalculatorTest {

    @Test void anEmptyString_ShouldReturnZero() {
        assertEquals(0, StringCalculator.add(""));
    }

    @Test void aSingleNumber_ShouldReturnTheValue() {
        assertEquals(7, StringCalculator.add("7"));
        assertEquals(47, StringCalculator.add("47"));
        assertEquals(147, StringCalculator.add("147"));
    }

    @Test void twoNumbersCommaDelimited_ShouldReturnTheSum() {
        assertEquals(47, StringCalculator.add("20,27"));
    }

    @Test void twoNumbersNewlineDelimited_ShouldReturnTheSum() {
        assertEquals(35, StringCalculator.add("10\n25"));
    }

    @Test void threeNumbersDelimitedEitherWay_ShouldReturnTheSum() {
        assertEquals(40, StringCalculator.add("5,10\n25"));
        assertEquals(40, StringCalculator.add("5\n10\n25"));
        assertEquals(40, StringCalculator.add("5\n10,25"));
    }

    @Test void negativeNumbers_ShouldThrowAnException() {
        assertThrows(IllegalArgumentException.class, () -> StringCalculator.add("-1"));
    }

    @Test void negativeNumbersException_ShouldHaveDescriptiveMessage() {
        Throwable e = assertThrows(IllegalArgumentException.class, () -> StringCalculator.add("-1,-2\n-23"));
        assertEquals("Negatives not allowed: [-1, -2, -23]", e.getMessage());
    }

    @Test void numbersGreaterThan1000_ShouldBeIgnored() {
        assertEquals(0, StringCalculator.add("1001"));
        assertEquals(1000, StringCalculator.add("1000\n1001"));
        assertEquals(40, StringCalculator.add("5,10\n25,1001"));
    }

    @Test void aSingleCharDelimiter_CanBeDefined() {
        assertEquals(8, StringCalculator.add("//;\n1;2;5"));
        assertEquals(15, StringCalculator.add("//+\n1+2+3+4+5"));
    }

    @Test void aMultiCharDelimiter_CanBeDefined() {
        assertEquals(8, StringCalculator.add("//[plus]\n1plus2plus5"));

    }

    @Test void manySingleOrMultiCharDelimiters_CanBeDefinedEachWrappedInSquareBrackets() {
        assertEquals(30, StringCalculator.add("//[d][plus]\n1plus2plus5d10d12"));
    }

}