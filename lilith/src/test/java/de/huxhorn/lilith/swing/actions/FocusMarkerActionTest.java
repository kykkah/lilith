/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.MarkerContainsCondition;
import de.huxhorn.sulky.conditions.Condition;

class FocusMarkerActionTest extends AbstractBasicFilterActionTestBase {

    @Override
    protected BasicFilterAction createAction() {
        return new FocusMarkerAction("markerName");
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return MarkerContainsCondition.class;
    }
}
