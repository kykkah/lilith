/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.NDCContainsCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusNDCActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusNDCAction("ndcMessage", false);
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return NDCContainsCondition.class;
    }
}
