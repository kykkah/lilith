/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpResponseHeadersContainsCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusHttpResponseHeaderActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusHttpResponseHeaderAction("responseHeaderKey", "responseHeaderValue");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpResponseHeadersContainsCondition.class;
    }

    @Override
    protected boolean isExpectingAlternativeBehavior() {
        return true;
    }
}
