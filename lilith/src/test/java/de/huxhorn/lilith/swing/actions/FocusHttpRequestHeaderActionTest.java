/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpRequestHeadersContainsCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusHttpRequestHeaderActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusHttpRequestHeaderAction("requestHeaderKey", "requestHeaderValue");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpRequestHeadersContainsCondition.class;
    }

    @Override
    protected boolean isExpectingAlternativeBehavior() {
        return true;
    }
}
