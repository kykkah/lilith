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

package de.huxhorn.lilith.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.HttpStatus;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventWrapperCorpusTest {

    @Test
    void matchesAllSetNullInputThrows() {
        NullPointerException ex =
                assertThrows(NullPointerException.class, () -> EventWrapperCorpus.matchAllSet(null));
        assertEquals("corpus must not be null!", ex.getMessage());
    }

    @Test
    void matchesAllSetWithEmptyInputReturnsEmptySet() {
        Set<Integer> result = EventWrapperCorpus.matchAllSet(Collections.emptyList());

        assertEquals(Collections.emptySet(), new HashSet<>(result));
    }

    @Test
    void matchesAllSetWithSequentialInputReturnsIndices() {
        List<Object> input = new ArrayList<>();
        input.add("a");
        input.add("b");
        input.add("c");

        Set<Integer> result = EventWrapperCorpus.matchAllSet(input);

        Set<Integer> expected = new HashSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);

        assertEquals(expected, new HashSet<>(result));
    }

    @Test
    void matchesAllSetWithoutArgumentsReturnsFullCorpusIndices() {
        Set<Integer> expected = EventWrapperCorpus.matchAllSet(EventWrapperCorpus.createCorpus());
        Set<Integer> actual = EventWrapperCorpus.matchAllSet();

        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    void corpusEntriesSupportBasicOperations() {
        List<Object> corpus = EventWrapperCorpus.createCorpus();
        assertNotNull(corpus);

        for (Object current : corpus) {
            if (current == null) {
                continue;
            }

            assertEquals(current, current);
            assertEquals(current.hashCode(), current.hashCode());
            assertNotNull(current.toString());
        }
    }

    @Test
    void unknownHttpStatusCodeIsStillUnknown() {
        List<Object> corpus = EventWrapperCorpus.createCorpus();
        assertTrue(corpus.size() > 120, "expected corpus entry at index 120");

        Object entry = corpus.get(120);
        assertTrue(entry instanceof EventWrapper, "expected EventWrapper at index 120");

        EventWrapper<?> wrapper = (EventWrapper<?>) entry;
        assertTrue(wrapper.getEvent() instanceof AccessEvent, "expected AccessEvent at index 120");

        AccessEvent accessEvent = (AccessEvent) wrapper.getEvent();
        Integer statusCode = accessEvent.getStatusCode();
        assertNotNull(statusCode);

        assertNull(HttpStatus.getStatus(statusCode));
    }
}
