/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.MDCContainsCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusMDCActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusMDCAction("mdcKey", "mdcValue");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return MDCContainsCondition.class;
    }

    @Override
    protected boolean isExpectingAlternativeBehavior() {
        return true;
    }
}
