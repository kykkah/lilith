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

class HttpStatusCodeConditionTest {

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        HttpStatusCodeCondition condition = new HttpStatusCodeCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ConditionTestSupport.setOf()),
                Arguments.of("", ConditionTestSupport.setOf()),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("1", ConditionTestSupport.setOf()),
                Arguments.of(" 1 ", ConditionTestSupport.setOf()),
                Arguments.of("100", ConditionTestSupport.setOf(52)),
                Arguments.of("200", ConditionTestSupport.setOf(53)),
                Arguments.of("202", ConditionTestSupport.setOf(54)),
                Arguments.of("301", ConditionTestSupport.setOf(55)),
                Arguments.of("404", ConditionTestSupport.setOf(56)),
                Arguments.of("451", ConditionTestSupport.setOf(57)),
                Arguments.of("500", ConditionTestSupport.setOf(58)),
                Arguments.of(" 100 ", ConditionTestSupport.setOf(52)),
                Arguments.of(" 200 ", ConditionTestSupport.setOf(53)),
                Arguments.of(" 202 ", ConditionTestSupport.setOf(54)),
                Arguments.of(" 301 ", ConditionTestSupport.setOf(55)),
                Arguments.of(" 404 ", ConditionTestSupport.setOf(56)),
                Arguments.of(" 451 ", ConditionTestSupport.setOf(57)),
                Arguments.of(" 500 ", ConditionTestSupport.setOf(58)));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        HttpStatusCodeCondition condition = new HttpStatusCodeCondition();
        condition.setSearchString(searchString);

        HttpStatusCodeCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStatusCode(), result.getStatusCode());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        HttpStatusCodeCondition condition = new HttpStatusCodeCondition();
        condition.setSearchString(searchString);

        HttpStatusCodeCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStatusCode(), result.getStatusCode());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        HttpStatusCodeCondition condition = new HttpStatusCodeCondition();
        condition.setSearchString(searchString);

        HttpStatusCodeCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStatusCode(), result.getStatusCode());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", "value", "404");
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void invalidValuesProduceInvalidCode(String input) {
        HttpStatusCodeCondition condition = new HttpStatusCodeCondition(input);

        assertEquals(HttpStatusCodeCondition.INVALID_CODE, condition.getStatusCode());
    }

    private static Stream<String> invalidValues() {
        return Stream.of(null, "", "foo", "99", "600");
    }

    @Test
    void equalsBehavesAsExpected() {
        HttpStatusCodeCondition instance = new HttpStatusCodeCondition();
        HttpStatusCodeCondition other = new HttpStatusCodeCondition("404");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
