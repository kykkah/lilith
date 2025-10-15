/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2017 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.huxhorn.lilith.engine.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.eventsource.SourceIdentifier;
import de.huxhorn.sulky.buffers.Buffer;
import de.huxhorn.sulky.conditions.Condition;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

class EventSourceImplTest {

    @Test
    void constructorWithNullSourceIdentifierFailsAsExpected() {
        Buffer<EventWrapper<Integer>> buffer = new DummyBuffer<>();
        Condition condition = new DummyCondition();

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new EventSourceImpl<>(null, buffer, condition, false));
        assertEquals("sourceIdentifier must not be null!", ex.getMessage());
    }

    @Test
    void constructorWithNullBufferFailsAsExpected() {
        SourceIdentifier sourceIdentifier = new SourceIdentifier("foo");
        Condition condition = new DummyCondition();

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new EventSourceImpl<>(sourceIdentifier, null, condition, false));
        assertEquals("buffer must not be null!", ex.getMessage());
    }

    @Test
    void constructorWithNullConditionDoesNotFail() {
        SourceIdentifier sourceIdentifier = new SourceIdentifier("foo");
        Buffer<EventWrapper<Integer>> buffer = new DummyBuffer<>();

        assertDoesNotThrow(() -> new EventSourceImpl<>(sourceIdentifier, buffer, false));
        assertDoesNotThrow(() -> new EventSourceImpl<>(sourceIdentifier, buffer, null, false));
    }

    private static final class DummyBuffer<T extends Serializable> implements Buffer<EventWrapper<T>> {

        @Override
        public EventWrapper<T> get(long index) {
            return null;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public Iterator<EventWrapper<T>> iterator() {
            return Collections.emptyIterator();
        }
    }

    private static final class DummyCondition implements Condition {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isTrue(Object element) {
            return false;
        }

        @Override
        public DummyCondition clone() {
            return this;
        }
    }
}
