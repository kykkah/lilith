/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FocusLoggerMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new FocusLoggerMenu();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(13, 14);
    }

    @ParameterizedTest(name = "prepareLoggerNames({0}) produces {1}")
    @MethodSource("loggerNames")
    void prepareLoggerNamesProducesExpectedValues(String loggerName, List<String> expectedResult) {
        assertEquals(expectedResult, FocusLoggerMenu.prepareLoggerNames(loggerName));
    }

    private static Stream<Arguments> loggerNames() {
        return Stream.of(
                Arguments.of("", List.of()),
                Arguments.of("Foo", List.of("Foo")),
                Arguments.of("foo.Bar", List.of("foo.Bar", "foo")),
                Arguments.of("foo.Bar$Foobar", List.of("foo.Bar.Foobar", "foo.Bar", "foo"))
        );
    }
}
