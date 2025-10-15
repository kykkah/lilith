/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.LevelCondition;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.sulky.conditions.Condition;

class FocusLevelActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusLevelAction(LoggingEvent.Level.INFO);
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return LevelCondition.class;
    }
}
