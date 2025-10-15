/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.swing.ApplicationPreferences;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class FocusMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        ApplicationPreferences applicationPreferences = mock(ApplicationPreferences.class);
        return new FocusMenu(applicationPreferences, false);
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return EventWrapperCorpus.matchAnyLoggingOrAccessEventSet();
    }

    @Override
    protected int expectedGetSelectedEventCalls() {
        return 23;
    }

    @Test
    void setConditionNamesDoesNotThrow() {
        FocusMenu menu = (FocusMenu) createMenu();

        assertDoesNotThrow(() -> {
            menu.setConditionNames(List.of("foo", "bar"));
            menu.setConditionNames(Collections.emptyList());
            menu.setConditionNames(null);
        });
    }
}
