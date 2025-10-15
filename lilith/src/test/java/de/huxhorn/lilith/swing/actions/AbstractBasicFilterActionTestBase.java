/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.swing.ViewContainer;
import de.huxhorn.sulky.conditions.Condition;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class AbstractBasicFilterActionTestBase {

    protected abstract BasicFilterAction createAction();

    protected abstract Class<? extends Condition> expectedConditionClass();

    @Test
    void isEnabledWithViewContainerWorksAsExpected() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        BasicFilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);

        assertTrue(filterAction.isEnabled());
    }

    @Test
    void isEnabledWithoutViewContainerWorksAsExpected() {
        BasicFilterAction filterAction = createAction();

        assertFalse(filterAction.isEnabled());
    }

    @Test
    void resolveConditionWithViewContainerWorksAsExpected() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        BasicFilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);

        Condition condition = filterAction.resolveCondition(null);

        assertNotNull(condition);
        assertEquals(expectedConditionClass(), condition.getClass());
    }

    @Test
    void resolveConditionWithoutViewContainerWorksAsExpected() {
        BasicFilterAction filterAction = createAction();

        Condition condition = filterAction.resolveCondition(null);

        assertNull(condition);
    }

    @Test
    void actionPerformedWithViewContainerWorksAsExpected() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        BasicFilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        filterAction.actionPerformed(actionEvent);

        verify(viewContainer, times(1)).applyCondition(any(), eq(actionEvent));
    }

    @Test
    void actionPerformedWithoutViewContainerWorksAsExpected() {
        BasicFilterAction filterAction = createAction();
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        assertDoesNotThrow(() -> filterAction.actionPerformed(actionEvent));
    }

    @Test
    void settingAndGettingViewContainerWorksAsExpected() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        BasicFilterAction filterAction = createAction();

        assertNull(filterAction.getViewContainer());

        filterAction.setViewContainer(viewContainer);
        assertEquals(viewContainer, filterAction.getViewContainer());

        filterAction.setViewContainer(null);
        assertNull(filterAction.getViewContainer());
    }

    @Test
    void resolveConditionWithDifferentActionEventsBehavesAsExpected() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        BasicFilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);

        ActionEvent defaultEvent = new ActionEvent("Foo", 0, null);
        ActionEvent alternativeEvent = new ActionEvent("Foo", 0, null, ActionEvent.ALT_MASK);

        Condition nullEventCondition = filterAction.resolveCondition(null);
        Condition defaultEventCondition = filterAction.resolveCondition(defaultEvent);
        Condition alternativeEventCondition = filterAction.resolveCondition(alternativeEvent);

        assertEquals(defaultEventCondition, nullEventCondition);
        if (defaultEventCondition != null && isExpectingAlternativeBehavior()) {
            assertNotEquals(defaultEventCondition, alternativeEventCondition);
        } else {
            assertEquals(defaultEventCondition, alternativeEventCondition);
        }
    }

    protected boolean isExpectingAlternativeBehavior() {
        return false;
    }
}
