/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.ThreadNameCondition;
import de.huxhorn.lilith.swing.preferences.SavedCondition;
import de.huxhorn.sulky.conditions.Condition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FocusSavedConditionActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        SavedCondition savedCondition = new SavedCondition(new ThreadNameCondition());
        savedCondition.setName("savedCondition");
        return new FocusSavedConditionAction(savedCondition, false);
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return ThreadNameCondition.class;
    }

    @Test
    void brokenSavedConditionThrowsExpectedException() {
        SavedCondition savedCondition = new SavedCondition();
        savedCondition.setName("savedCondition");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new FocusSavedConditionAction(savedCondition, false));
        assertEquals("Condition of " + savedCondition + " is null!", exception.getMessage());
    }
}
