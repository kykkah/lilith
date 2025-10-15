/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpStatusCodeCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusHttpStatusCodeActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusHttpStatusCodeAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(52, 53, 54, 55, 56, 57, 58, 120);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of("100", "200", "202", "301", "404", "451", "500", "488");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpStatusCodeCondition.class;
    }
}
