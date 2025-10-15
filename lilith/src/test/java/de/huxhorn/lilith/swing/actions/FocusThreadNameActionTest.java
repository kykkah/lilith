/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.ThreadNameCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusThreadNameActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusThreadNameAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(83);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of("threadName");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return ThreadNameCondition.class;
    }
}
