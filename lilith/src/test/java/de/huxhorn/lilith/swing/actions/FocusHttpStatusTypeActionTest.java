/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpStatusTypeCondition;
import de.huxhorn.lilith.data.access.HttpStatus;
import de.huxhorn.sulky.conditions.Condition;

class FocusHttpStatusTypeActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusHttpStatusTypeAction(HttpStatus.Type.CLIENT_ERROR);
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpStatusTypeCondition.class;
    }
}
