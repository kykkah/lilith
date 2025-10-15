/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.ThrowableCondition;
import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.sulky.conditions.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class FocusThrowablesActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusThrowablesAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return EventWrapperCorpus.matchAnyLoggingEventSet();
    }

    @Override
    protected List<String> expectedSearchStrings() {
        List<String> result = new ArrayList<>();
        expectedEnabledIndices().forEach(index -> result.add(null));
        return result;
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return ThrowableCondition.class;
    }
}
