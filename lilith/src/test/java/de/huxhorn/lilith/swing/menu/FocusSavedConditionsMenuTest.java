/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.swing.ApplicationPreferences;
import de.huxhorn.lilith.swing.preferences.SavedCondition;
import de.huxhorn.sulky.conditions.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FocusSavedConditionsMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        ApplicationPreferences applicationPreferences = mock(ApplicationPreferences.class);
        SavedCondition savedCondition = new SavedCondition(mock(Condition.class));
        when(applicationPreferences.getConditionNames()).thenReturn(List.of("foo", "bar"));
        when(applicationPreferences.resolveSavedCondition(anyString())).thenReturn(savedCondition);
        return new FocusSavedConditionsMenu(applicationPreferences, false);
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return EventWrapperCorpus.matchAnyEventWrapperSet();
    }

    @ParameterizedTest(name = "broken saved condition handled for eventWrapper={0}")
    @MethodSource("eventWrappers")
    void brokenSavedConditionsAreHandledGracefully(EventWrapper eventWrapper) {
        SavedCondition brokenCondition = new SavedCondition(null);
        ApplicationPreferences applicationPreferences = mock(ApplicationPreferences.class);
        when(applicationPreferences.getConditionNames()).thenReturn(List.of("foo", "bar"));
        when(applicationPreferences.resolveSavedCondition(anyString())).thenReturn(brokenCondition);
        FocusSavedConditionsMenu menu = new FocusSavedConditionsMenu(applicationPreferences, false);
        menu.setEventWrapper(eventWrapper);

        menu.setConditionNames(List.of("foo", "bar"));
        assertFalse(menu.isEnabled());

        menu.setConditionNames(null);
        assertFalse(menu.isEnabled());
    }

    private static Stream<Arguments> eventWrappers() {
        return Stream.of(
                Arguments.of((EventWrapper) null),
                Arguments.of(new EventWrapper())
        );
    }
}
