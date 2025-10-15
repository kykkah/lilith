/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.CallLocationCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusCallLocationActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusCallLocationAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(44, 45, 46, 47, 48, 49, 50, 51, 66);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of(
                "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)",
                "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)",
                "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)",
                "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)",
                "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)",
                "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)",
                "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)",
                "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)",
                "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)"
        );
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return CallLocationCondition.class;
    }
}
