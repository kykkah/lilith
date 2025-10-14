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

class HttpStatusTypeConditionTest {

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        HttpStatusTypeCondition condition = new HttpStatusTypeCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ConditionTestSupport.setOf()),
                Arguments.of("", ConditionTestSupport.setOf()),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("INFORMATIONAL", ConditionTestSupport.setOf(52)),
                Arguments.of("SUCCESSFUL", ConditionTestSupport.setOf(53, 54)),
                Arguments.of("REDIRECTION", ConditionTestSupport.setOf(55)),
                Arguments.of("CLIENT_ERROR", ConditionTestSupport.setOf(56, 57, 120)),
                Arguments.of("SERVER_ERROR", ConditionTestSupport.setOf(58)),
                Arguments.of("info", ConditionTestSupport.setOf(52)),
                Arguments.of("succ", ConditionTestSupport.setOf(53, 54)),
                Arguments.of("red", ConditionTestSupport.setOf(55)),
                Arguments.of("cl", ConditionTestSupport.setOf(56, 57, 120)),
                Arguments.of("se", ConditionTestSupport.setOf(58)),
                Arguments.of("1", ConditionTestSupport.setOf(52)),
                Arguments.of("2", ConditionTestSupport.setOf(53, 54)),
                Arguments.of("3", ConditionTestSupport.setOf(55)),
                Arguments.of("4", ConditionTestSupport.setOf(56, 57, 120)),
                Arguments.of("5", ConditionTestSupport.setOf(58)),
                Arguments.of(" 1 ", ConditionTestSupport.setOf(52)),
                Arguments.of(" 2 ", ConditionTestSupport.setOf(53, 54)),
                Arguments.of(" 3 ", ConditionTestSupport.setOf(55)),
                Arguments.of(" 4 ", ConditionTestSupport.setOf(56, 57, 120)),
                Arguments.of(" 5 ", ConditionTestSupport.setOf(58)),
                Arguments.of("1X", ConditionTestSupport.setOf(52)),
                Arguments.of("2X", ConditionTestSupport.setOf(53, 54)),
                Arguments.of("3X", ConditionTestSupport.setOf(55)),
                Arguments.of("4X", ConditionTestSupport.setOf(56, 57, 120)),
                Arguments.of("5X", ConditionTestSupport.setOf(58)),
                Arguments.of("1xx", ConditionTestSupport.setOf(52)),
                Arguments.of("2xx", ConditionTestSupport.setOf(53, 54)),
                Arguments.of("3xx", ConditionTestSupport.setOf(55)),
                Arguments.of("4xx", ConditionTestSupport.setOf(56, 57, 120)),
                Arguments.of("5xx", ConditionTestSupport.setOf(58)));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        HttpStatusTypeCondition condition = new HttpStatusTypeCondition();
        condition.setSearchString(searchString);

        HttpStatusTypeCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStatusType(), result.getStatusType());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        HttpStatusTypeCondition condition = new HttpStatusTypeCondition();
        condition.setSearchString(searchString);

        HttpStatusTypeCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStatusType(), result.getStatusType());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        HttpStatusTypeCondition condition = new HttpStatusTypeCondition();
        condition.setSearchString(searchString);

        HttpStatusTypeCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStatusType(), result.getStatusType());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", "value", "4xx");
    }

    @Test
    void equalsBehavesAsExpected() {
        HttpStatusTypeCondition instance = new HttpStatusTypeCondition();
        HttpStatusTypeCondition other = new HttpStatusTypeCondition("4xx");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
