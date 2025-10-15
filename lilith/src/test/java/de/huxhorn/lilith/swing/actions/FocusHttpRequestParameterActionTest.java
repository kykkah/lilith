/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpRequestParametersContainsCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusHttpRequestParameterActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusHttpRequestParameterAction("requestParameterKey", "requestParameterValue");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpRequestParametersContainsCondition.class;
    }

    @Override
    protected boolean isExpectingAlternativeBehavior() {
        return true;
    }
}
