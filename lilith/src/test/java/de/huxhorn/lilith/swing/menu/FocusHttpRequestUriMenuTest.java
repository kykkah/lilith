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

class FocusHttpRequestUriMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new FocusHttpRequestUriMenu();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(73, 122);
    }

    @ParameterizedTest(name = "prepareUris({0}) produces {1}")
    @MethodSource("uris")
    void prepareUrisProducesExpectedValues(String uri, List<String> expectedResult) {
        assertEquals(expectedResult, FocusHttpRequestUriMenu.prepareUris(uri));
    }

    private static Stream<Arguments> uris() {
        return Stream.of(
                Arguments.of("", List.of()),
                Arguments.of("/", List.of()),
                Arguments.of("/foo", List.of("/foo")),
                Arguments.of("/foo/bar", List.of("/foo/bar", "/foo")),
                Arguments.of("/foo/bar/foobar", List.of("/foo/bar/foobar", "/foo/bar", "/foo"))
        );
    }
}
