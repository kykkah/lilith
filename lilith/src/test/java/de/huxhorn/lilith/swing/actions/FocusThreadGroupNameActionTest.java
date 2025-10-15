/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.ThreadGroupNameCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusThreadGroupNameActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusThreadGroupNameAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(85);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of("groupName");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return ThreadGroupNameCondition.class;
    }
}
