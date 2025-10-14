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

package de.huxhorn.lilith.conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import de.huxhorn.lilith.data.logging.LoggingEvent;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LevelConditionTest {

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        LevelCondition condition = new LevelCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ConditionTestSupport.setOf()),
                Arguments.of("", ConditionTestSupport.setOf()),
                Arguments.of("FOO", ConditionTestSupport.setOf()),
                Arguments.of("TRACE", ConditionTestSupport.setOf(8, 9, 10, 11, 12)),
                Arguments.of("DEBUG", ConditionTestSupport.setOf(9, 10, 11, 12)),
                Arguments.of("INFO", ConditionTestSupport.setOf(10, 11, 12)),
                Arguments.of("WARN", ConditionTestSupport.setOf(11, 12)),
                Arguments.of("ERROR", ConditionTestSupport.setOf(12)));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        LevelCondition condition = new LevelCondition();
        condition.setSearchString(searchString);

        LevelCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getLevel(), result.getLevel());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        LevelCondition condition = new LevelCondition();
        condition.setSearchString(searchString);

        LevelCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getLevel(), result.getLevel());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        LevelCondition condition = new LevelCondition();
        condition.setSearchString(searchString);

        LevelCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getLevel(), result.getLevel());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", "value", LoggingEvent.Level.INFO.name());
    }

    @Test
    void equalsBehavesAsExpected() {
        LevelCondition instance = new LevelCondition();
        LevelCondition other = new LevelCondition("INFO");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
