/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.ThrowableCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusThrowableActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusThrowableAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(25, 26, 27, 28, 29, 30, 93, 94, 115);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of(
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "recursiveCause",
                "recursiveSuppressed",
                ""
        );
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return ThrowableCondition.class;
    }
}
