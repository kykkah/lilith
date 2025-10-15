/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.SearchStringCondition;
import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.swing.ViewContainer;
import de.huxhorn.sulky.conditions.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class AbstractFilterActionTestBase {

    protected abstract FilterAction createAction();

    protected abstract Set<Integer> expectedEnabledIndices();

    protected abstract List<String> expectedSearchStrings();

    protected abstract Class<? extends Condition> expectedConditionClass();

    @Test
    void isEnabledWithViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        ViewContainer viewContainer = mock(ViewContainer.class);
        FilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);
        Set<Integer> enabledIndices = new HashSet<>();

        for (int i = 0; i < corpus.size(); i++) {
            Object current = corpus.get(i);
            if (current instanceof EventWrapper) {
                filterAction.setEventWrapper((EventWrapper) current);
                if (filterAction.isEnabled()) {
                    enabledIndices.add(i);
                }
            }
        }

        assertEquals(new TreeSet<>(expectedEnabledIndices()), new TreeSet<>(enabledIndices));
    }

    @Test
    void isEnabledWithoutViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        FilterAction filterAction = createAction();
        Set<Integer> enabledIndices = new HashSet<>();

        for (int i = 0; i < corpus.size(); i++) {
            Object current = corpus.get(i);
            if (current instanceof EventWrapper) {
                filterAction.setEventWrapper((EventWrapper) current);
                if (filterAction.isEnabled()) {
                    enabledIndices.add(i);
                }
            }
        }

        assertEquals(new TreeSet<>(expectedEnabledIndices()), new TreeSet<>(enabledIndices));
    }

    @Test
    void resolveConditionWithViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        ViewContainer viewContainer = mock(ViewContainer.class);
        FilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);
        List<String> searchStrings = new ArrayList<>();
        Set<String> conditionClasses = new HashSet<>();

        for (Object entry : corpus) {
            if (entry instanceof EventWrapper) {
                filterAction.setEventWrapper((EventWrapper) entry);
                Condition condition = filterAction.resolveCondition(null);
                if (condition == null) {
                    continue;
                }
                if (condition instanceof SearchStringCondition) {
                    searchStrings.add(((SearchStringCondition) condition).getSearchString());
                } else {
                    searchStrings.add(condition.toString());
                }
                conditionClasses.add(condition.getClass().getName());
            }
        }

        assertEquals(expectedSearchStrings(), searchStrings);
        assertEquals(1, conditionClasses.size());
        assertTrue(conditionClasses.contains(expectedConditionClass().getName()));
    }

    @Test
    void resolveConditionWithoutViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        FilterAction filterAction = createAction();
        List<String> searchStrings = new ArrayList<>();
        Set<String> conditionClasses = new HashSet<>();

        for (Object entry : corpus) {
            if (entry instanceof EventWrapper) {
                filterAction.setEventWrapper((EventWrapper) entry);
                Condition condition = filterAction.resolveCondition(null);
                if (condition == null) {
                    continue;
                }
                if (condition instanceof SearchStringCondition) {
                    searchStrings.add(((SearchStringCondition) condition).getSearchString());
                } else {
                    searchStrings.add(condition.toString());
                }
                conditionClasses.add(condition.getClass().getName());
            }
        }

        assertEquals(expectedSearchStrings(), searchStrings);
        assertEquals(1, conditionClasses.size());
        assertTrue(conditionClasses.contains(expectedConditionClass().getName()));
    }

    @Test
    void actionPerformedWithViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        ViewContainer viewContainer = mock(ViewContainer.class);
        FilterAction filterAction = createAction();
        filterAction.setViewContainer(viewContainer);
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        for (Object entry : corpus) {
            if (entry instanceof EventWrapper) {
                filterAction.setEventWrapper((EventWrapper) entry);
                filterAction.actionPerformed(actionEvent);
            }
        }

        verify(viewContainer, times(expectedSearchStrings().size())).applyCondition(any(), eq(actionEvent));
    }

    @Test
    void actionPerformedWithoutViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        FilterAction filterAction = createAction();
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);

        assertDoesNotThrow(() -> {
            for (Object entry : corpus) {
                if (entry instanceof EventWrapper) {
                    filterAction.setEventWrapper((EventWrapper) entry);
                    filterAction.actionPerformed(actionEvent);
                }
            }
        });
    }

    @Test
    void settingAndGettingViewContainerWorksAsExpected() {
        ViewContainer viewContainer = mock(ViewContainer.class);
        FilterAction filterAction = createAction();

        assertNull(filterAction.getViewContainer());

        filterAction.setViewContainer(viewContainer);
        assertEquals(viewContainer, filterAction.getViewContainer());

        filterAction.setViewContainer(null);
        assertNull(filterAction.getViewContainer());
    }

    @ParameterizedTest(name = "resolveCondition behavior for corpus entry #{index}")
    @MethodSource("corpusEntries")
    void resolveConditionWithAndWithoutActionEventWorksAsExpected(Object value) {
        FilterAction filterAction = createAction();
        ActionEvent actionEvent = new ActionEvent("Foo", 0, null);
        ActionEvent altActionEvent = new ActionEvent("Foo", 0, null, ActionEvent.ALT_MASK);

        Condition nullEventCondition = null;
        Condition actionEventCondition = null;
        Condition altEventCondition = null;

        if (value instanceof EventWrapper) {
            filterAction.setEventWrapper((EventWrapper) value);
            nullEventCondition = filterAction.resolveCondition(null);
            actionEventCondition = filterAction.resolveCondition(actionEvent);
            altEventCondition = filterAction.resolveCondition(altActionEvent);
        }

        assertEquals(actionEventCondition, nullEventCondition);
        if (actionEventCondition != null && isExpectingAlternativeBehavior()) {
            assertNotEquals(actionEventCondition, altEventCondition);
        } else {
            assertEquals(actionEventCondition, altEventCondition);
        }
    }

    @Test
    void sanityCheckExpectedValues() {
        assertEquals(expectedEnabledIndices().size(), expectedSearchStrings().size());
    }

    protected boolean isExpectingAlternativeBehavior() {
        return false;
    }

    protected static Stream<Object> corpusEntries() {
        return EventWrapperCorpus.createCorpus().stream();
    }
}
