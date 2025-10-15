/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.Action;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ActionTooltipsTest {

    @ParameterizedTest(name = "initializeCroppedTooltip({0}, html={1})")
    @MethodSource("tooltipArguments")
    void initializeCroppedTooltipSetsExpectedValue(String input, boolean html, String expectedTooltip) {
        Action action = mock(Action.class);

        ActionTooltips.initializeCroppedTooltip(input, action, html);

        verify(action).putValue(Action.SHORT_DESCRIPTION, expectedTooltip);
    }

    private static Stream<Arguments> tooltipArguments() {
        return Stream.of(
                Arguments.of(null, true, null),
                Arguments.of(null, false, null),
                Arguments.of("", true, "<html><tt><pre></pre></tt></html>"),
                Arguments.of("", false, ""),
                Arguments.of("foo", true, "<html><tt><pre>foo</pre></tt></html>"),
                Arguments.of("foo", false, "foo")
        );
    }
}
