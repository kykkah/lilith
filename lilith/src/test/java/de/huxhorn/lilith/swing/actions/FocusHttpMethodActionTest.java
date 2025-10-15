/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpMethodCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusHttpMethodActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusHttpMethodAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(70, 71);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of("GET", "PUT");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpMethodCondition.class;
    }
}
