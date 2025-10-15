/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.swing.ViewContainer;
import de.huxhorn.sulky.conditions.Condition;
import de.huxhorn.sulky.conditions.Not;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class NegateFilterActionTest {

    @Test
    void defaultConstructorLeavesWrappedNull() {
        NegateFilterAction action = new NegateFilterAction();

        assertNull(action.getWrapped());
    }

    @Test
    void constructorWithFilterActionSetsWrapped() {
        FilterAction filterAction = mock(FilterAction.class);

        NegateFilterAction action = new NegateFilterAction(filterAction);

        assertSame(filterAction, action.getWrapped());
    }

    @Test
    void setWrappedUpdatesValue() {
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.setWrapped(filterAction);

        assertSame(filterAction, action.getWrapped());
    }

    @Test
    void resolveConditionWithoutWrappedReturnsNull() {
        NegateFilterAction action = new NegateFilterAction();

        assertNull(action.resolveCondition(null));
    }

    @Test
    void resolveConditionWithWrappedReturningNullReturnsNull() {
        FilterAction filterAction = mock(FilterAction.class);
        when(filterAction.resolveCondition(null)).thenReturn(null);
        NegateFilterAction action = new NegateFilterAction(filterAction);

        assertNull(action.resolveCondition(null));
        verify(filterAction).resolveCondition(null);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void resolveConditionWrapsConditionInNot() {
        FilterAction filterAction = mock(FilterAction.class);
        Condition mockedCondition = mock(Condition.class);
        when(filterAction.resolveCondition(null)).thenReturn(mockedCondition);
        NegateFilterAction action = new NegateFilterAction(filterAction);

        Condition result = action.resolveCondition(null);

        assertTrue(result instanceof Not);
        assertSame(mockedCondition, ((Not) result).getCondition());
        verify(filterAction).resolveCondition(null);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void getValuePropagatesToWrappedAction() {
        FilterAction filterAction = mock(FilterAction.class);
        when(filterAction.getValue("foo")).thenReturn("bar");
        NegateFilterAction action = new NegateFilterAction();

        assertNull(action.getValue("foo"));

        action.setWrapped(filterAction);
        assertEquals("bar", action.getValue("foo"));

        action.setWrapped(null);
        assertNull(action.getValue("foo"));

        verify(filterAction).getValue("foo");
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void putValuePropagatesWhenWrappedPresent() {
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.putValue("foo", "bar");

        action.setWrapped(filterAction);
        action.putValue("foo", "bar");

        action.setWrapped(null);
        action.putValue("foo", "bar");

        verify(filterAction).putValue("foo", "bar");
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void isEnabledPropagatesResults() {
        FilterAction filterAction = mock(FilterAction.class);
        when(filterAction.isEnabled()).thenReturn(true, false);
        NegateFilterAction action = new NegateFilterAction();

        assertFalse(action.isEnabled());

        action.setWrapped(filterAction);
        assertTrue(action.isEnabled());
        assertFalse(action.isEnabled());

        action.setWrapped(null);
        assertFalse(action.isEnabled());

        verify(filterAction, times(2)).isEnabled();
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void setEnabledPropagates() {
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.setEnabled(true);

        action.setWrapped(filterAction);
        action.setEnabled(true);

        action.setWrapped(null);
        action.setEnabled(true);

        verify(filterAction).setEnabled(true);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void addPropertyChangeListenerPropagates() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.addPropertyChangeListener(listener);

        action.setWrapped(filterAction);
        action.addPropertyChangeListener(listener);

        action.setWrapped(null);
        action.addPropertyChangeListener(listener);

        verify(filterAction).addPropertyChangeListener(listener);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void removePropertyChangeListenerPropagates() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.removePropertyChangeListener(listener);

        action.setWrapped(filterAction);
        action.removePropertyChangeListener(listener);

        action.setWrapped(null);
        action.removePropertyChangeListener(listener);

        verify(filterAction).removePropertyChangeListener(listener);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void setEventWrapperPropagatesForFilterAction() {
        EventWrapper eventWrapper = mock(EventWrapper.class);
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.setEventWrapper(eventWrapper);

        action.setWrapped(filterAction);
        action.setEventWrapper(eventWrapper);

        action.setWrapped(null);
        action.setEventWrapper(eventWrapper);

        verify(filterAction).setEventWrapper(eventWrapper);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void setEventWrapperIgnoredForBasicFilterAction() {
        EventWrapper eventWrapper = mock(EventWrapper.class);
        BasicFilterAction basicFilterAction = mock(BasicFilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.setEventWrapper(eventWrapper);

        action.setWrapped(basicFilterAction);
        assertDoesNotThrow(() -> action.setEventWrapper(eventWrapper));

        action.setWrapped(null);
        action.setEventWrapper(eventWrapper);

        verifyNoInteractions(basicFilterAction);
    }

    @Test
    void setViewContainerPropagates() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        FilterAction filterAction = mock(FilterAction.class);
        NegateFilterAction action = new NegateFilterAction();

        action.setViewContainer(viewContainer);

        action.setWrapped(filterAction);
        action.setViewContainer(viewContainer);

        action.setWrapped(null);
        action.setViewContainer(viewContainer);

        verify(filterAction).setViewContainer(viewContainer);
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void getViewContainerPropagates() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        FilterAction filterAction = mock(FilterAction.class);
        when(filterAction.getViewContainer()).thenReturn(viewContainer);
        NegateFilterAction action = new NegateFilterAction();

        assertNull(action.getViewContainer());

        action.setWrapped(filterAction);
        assertSame(viewContainer, action.getViewContainer());

        action.setWrapped(null);
        assertNull(action.getViewContainer());

        verify(filterAction).getViewContainer();
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void actionPerformedAppliesNegatedConditionWhenAvailable() {
        FilterAction filterAction = mock(FilterAction.class);
        ViewContainer viewContainer = mock(ViewContainer.class);
        Condition mockedCondition = mock(Condition.class);
        when(filterAction.getViewContainer()).thenReturn(viewContainer);
        when(filterAction.resolveCondition(any())).thenReturn(mockedCondition);
        NegateFilterAction action = new NegateFilterAction(filterAction);
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        action.actionPerformed(actionEvent);

        ArgumentCaptor<Condition> conditionCaptor = ArgumentCaptor.forClass(Condition.class);
        verify(viewContainer).applyCondition(conditionCaptor.capture(), eq(actionEvent));
        Condition appliedCondition = conditionCaptor.getValue();
        assertTrue(appliedCondition instanceof Not);
        assertSame(mockedCondition, ((Not) appliedCondition).getCondition());

        verify(filterAction).getViewContainer();
        verify(filterAction).resolveCondition(actionEvent);
        verifyNoMoreInteractions(filterAction, viewContainer);
    }

    @Test
    void actionPerformedDoesNothingWhenWrappedOrViewContainerMissing() {
        FilterAction filterAction = mock(FilterAction.class);
        when(filterAction.getViewContainer()).thenReturn(null);
        NegateFilterAction action = new NegateFilterAction();
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        action.actionPerformed(actionEvent);

        action.setWrapped(filterAction);
        action.actionPerformed(actionEvent);

        action.setWrapped(null);
        action.actionPerformed(actionEvent);

        verify(filterAction).getViewContainer();
        verifyNoMoreInteractions(filterAction);
    }

    @Test
    void actionPerformedDoesNothingWhenConditionMissing() {
        FilterAction filterAction = mock(FilterAction.class);
        ViewContainer viewContainer = mock(ViewContainer.class);
        when(filterAction.getViewContainer()).thenReturn(viewContainer);
        when(filterAction.resolveCondition(any())).thenReturn(null);
        NegateFilterAction action = new NegateFilterAction(filterAction);
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        action.actionPerformed(actionEvent);

        verify(filterAction).getViewContainer();
        verify(filterAction).resolveCondition(actionEvent);
        verifyNoInteractions(viewContainer);
        verifyNoMoreInteractions(filterAction);
    }
}
