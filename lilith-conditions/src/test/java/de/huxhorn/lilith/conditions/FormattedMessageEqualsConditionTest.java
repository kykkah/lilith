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

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FormattedMessageEqualsConditionTest {

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        FormattedMessageEqualsCondition condition = new FormattedMessageEqualsCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ConditionTestSupport.setOf()),
                Arguments.of("", ConditionCorpus.matchAllSet()),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("message", ConditionTestSupport.setOf()),
                Arguments.of("a message", ConditionTestSupport.setOf()),
                Arguments.of("another message", ConditionTestSupport.setOf()),
                Arguments.of("a message.", ConditionTestSupport.setOf(17)),
                Arguments.of("another message.", ConditionTestSupport.setOf(18)),
                Arguments.of("paramValue", ConditionTestSupport.setOf(22)),
                Arguments.of("{}", ConditionTestSupport.setOf(23)));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        FormattedMessageEqualsCondition condition = new FormattedMessageEqualsCondition();
        condition.setSearchString(searchString);

        FormattedMessageEqualsCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        FormattedMessageEqualsCondition condition = new FormattedMessageEqualsCondition();
        condition.setSearchString(searchString);

        FormattedMessageEqualsCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        FormattedMessageEqualsCondition condition = new FormattedMessageEqualsCondition();
        condition.setSearchString(searchString);

        FormattedMessageEqualsCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", "value");
    }

    @Test
    void equalsBehavesAsExpected() {
        FormattedMessageEqualsCondition instance = new FormattedMessageEqualsCondition();
        FormattedMessageEqualsCondition other = new FormattedMessageEqualsCondition("foo");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
