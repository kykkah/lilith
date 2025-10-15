/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpRequestUriStartsWithCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusHttpRequestUriActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusHttpRequestUriAction("/foo");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpRequestUriStartsWithCondition.class;
    }
}
