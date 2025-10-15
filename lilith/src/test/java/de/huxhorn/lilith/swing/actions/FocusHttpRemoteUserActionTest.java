/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpRemoteUserCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusHttpRemoteUserActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusHttpRemoteUserAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(59, 61, 62, 63, 64);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of("", " ", " - ", "sfalken", " sfalken ");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpRemoteUserCondition.class;
    }
}
