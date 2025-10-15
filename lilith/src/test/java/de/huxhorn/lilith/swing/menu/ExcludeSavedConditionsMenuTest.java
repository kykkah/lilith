/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import de.huxhorn.lilith.swing.ApplicationPreferences;
import de.huxhorn.lilith.swing.preferences.SavedCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExcludeSavedConditionsMenuTest extends FocusSavedConditionsMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        ApplicationPreferences applicationPreferences = mock(ApplicationPreferences.class);
        SavedCondition savedCondition = new SavedCondition(mock(Condition.class));
        when(applicationPreferences.getConditionNames()).thenReturn(List.of("foo", "bar"));
        when(applicationPreferences.resolveSavedCondition(anyString())).thenReturn(savedCondition);
        return new ExcludeSavedConditionsMenu(applicationPreferences, false);
    }
}
