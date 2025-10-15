/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.swing.ViewContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

abstract class AbstractFilterMenuTestBase {

    protected abstract AbstractFilterMenu createMenu();

    protected abstract Set<Integer> expectedEnabledIndices();

    protected int expectedGetSelectedEventCalls() {
        return 1;
    }

    @Test
    void isEnabledWithViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        ViewContainer viewContainer = mock(ViewContainer.class);
        AbstractFilterMenu menu = createMenu();
        menu.setViewContainer(viewContainer);
        Set<Integer> enabledIndices = new HashSet<>();

        for (int i = 0; i < corpus.size(); i++) {
            Object current = corpus.get(i);
            if (current instanceof EventWrapper) {
                menu.setEventWrapper((EventWrapper) current);
                if (menu.isEnabled()) {
                    enabledIndices.add(i);
                }
            }
        }

        assertEquals(new TreeSet<>(expectedEnabledIndices()), new TreeSet<>(enabledIndices));
    }

    @ParameterizedTest(name = "component count behavior for corpus entry #{index}")
    @MethodSource("corpusEntries")
    void isEnabledWithViewContainerComponentCountWorksAsExpected(Object entry) {
        ViewContainer viewContainer = mock(ViewContainer.class);
        AbstractFilterMenu menu = createMenu();
        menu.setViewContainer(viewContainer);

        boolean enabled = false;
        int componentCount = 0;
        if (entry instanceof EventWrapper) {
            menu.setEventWrapper((EventWrapper) entry);
            enabled = menu.isEnabled();
            componentCount = menu.getMenuComponentCount();
        }

        assertTrue(!enabled || componentCount != 0);
    }

    @Test
    void isEnabledWithoutViewContainerWorksAsExpected() {
        List<?> corpus = EventWrapperCorpus.createCorpus();
        AbstractFilterMenu menu = createMenu();
        Set<Integer> enabledIndices = new HashSet<>();

        for (int i = 0; i < corpus.size(); i++) {
            Object current = corpus.get(i);
            if (current instanceof EventWrapper) {
                menu.setEventWrapper((EventWrapper) current);
                if (menu.isEnabled()) {
                    enabledIndices.add(i);
                }
            }
        }

        assertEquals(new TreeSet<>(expectedEnabledIndices()), new TreeSet<>(enabledIndices));
    }

    @Test
    void settingAndGettingViewContainerWorksAsExpected() {
        AbstractFilterMenu menu = createMenu();
        ViewContainer viewContainer = mock(ViewContainer.class);

        menu.setViewContainer(null);
        assertNull(menu.getViewContainer());

        menu.setViewContainer(viewContainer);
        verify(viewContainer, times(expectedGetSelectedEventCalls())).getSelectedEvent();
        assertEquals(viewContainer, menu.getViewContainer());

        menu.setViewContainer(null);
        assertNull(menu.getViewContainer());
    }

    static Stream<Object> corpusEntries() {
        return EventWrapperCorpus.createCorpus().stream();
    }
}
