/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.MessagePatternEqualsCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusMessagePatternActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusMessagePatternAction(false);
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(19, 21, 22);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of(
                "a message with parameter {}.",
                "a message with parameter {} and unresolved parameter {}.",
                "{}"
        );
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return MessagePatternEqualsCondition.class;
    }
}
