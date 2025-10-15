/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.LoggerStartsWithCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusLoggerActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusLoggerAction("loggerName");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return LoggerStartsWithCondition.class;
    }
}
