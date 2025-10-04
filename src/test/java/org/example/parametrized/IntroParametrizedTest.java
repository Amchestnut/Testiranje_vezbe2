package org.example.parametrized;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class IntroParametrizedTest {

    // 1) Jednostavan ValueSource (jedan argument)
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void valueSource_ints_areBetween1and3(int argument) {
        assertTrue(argument > 0 && argument < 4);
    }

    // 2) Null/Empty/Blank primer za String
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void strings_nullEmptyOrBlank_ok(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }

    // 3) MethodSource
    @ParameterizedTest
    @MethodSource
    void localMethodSource_returnsNonNull(String argument) {
        assertNotNull(argument);
    }
    static Stream<String> localMethodSource_returnsNonNull() {
        return Stream.of("apple", "banana");
    }

    // 4) MethodSource sa vise argumenata (Arguments.of)
    @ParameterizedTest
    @MethodSource("squareData")
    void square_methodSource_ok(int a, int expectedSquare) {
        assertEquals(expectedSquare, a * a);
    }
    private static Stream<Arguments> squareData() {
        return Stream.of(
                Arguments.of(2, 4),
                Arguments.of(3, 9),
                Arguments.of(4, 16)
        );
    }


    // 5) MethodSource sa heterogenim tipovima
    @ParameterizedTest
    @MethodSource("heterogenicData")
    void hetero_methodSource_ok(int first, String second, List<String> third) {
        assertNotNull(first);
        assertNotNull(second);
        assertFalse(third.isEmpty());
    }
    private static Stream<Arguments> heterogenicData() {
        return Stream.of(
                Arguments.of(1, "foo", Arrays.asList("a", "b", "c")),
                Arguments.of(2, "bar", Arrays.asList("x", "y", "z"))
        );
    }

}
