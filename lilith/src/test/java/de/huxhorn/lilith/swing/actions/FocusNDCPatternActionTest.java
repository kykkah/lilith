/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.NDCContainsPatternCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusNDCPatternActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusNDCPatternAction("ndcMessagePattern", false);
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return NDCContainsPatternCondition.class;
    }
}
