/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.FormattedMessageEqualsCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusFormattedMessageActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusFormattedMessageAction(false);
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(17, 18, 19, 20, 21, 22, 23);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of(
                "a message.",
                "another message.",
                "a message with parameter paramValue.",
                "a message with unresolved parameter {}.",
                "a message with parameter paramValue and unresolved parameter {}.",
                "paramValue",
                "{}"
        );
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return FormattedMessageEqualsCondition.class;
    }
}
