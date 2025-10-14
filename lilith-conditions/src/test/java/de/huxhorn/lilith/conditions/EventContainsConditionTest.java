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

class EventContainsConditionTest {

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        EventContainsCondition condition = new EventContainsCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ConditionTestSupport.setOf()),
                Arguments.of("", ConditionCorpus.matchAllSet()),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("EBU", ConditionTestSupport.setOf(9)),
                Arguments.of("foo", ConditionTestSupport.setOf(13, 14, 74, 75)),
                Arguments.of("com", ConditionTestSupport.setOf(13, 14)),
                Arguments.of("com.foo", ConditionTestSupport.setOf(13, 14)),
                Arguments.of("com.foo.Foo", ConditionTestSupport.setOf(13)),
                Arguments.of("com.foo.Bar", ConditionTestSupport.setOf(14)),
                Arguments.of("message", ConditionTestSupport.setOf(17, 18, 19, 20, 21, 36, 37, 38, 39, 40)),
                Arguments.of("a message", ConditionTestSupport.setOf(17, 19, 20, 21, 36, 38, 39, 40)),
                Arguments.of("another message", ConditionTestSupport.setOf(18, 37)),
                Arguments.of("a message.", ConditionTestSupport.setOf(17, 36)),
                Arguments.of("another message.", ConditionTestSupport.setOf(18, 37)),
                Arguments.of("paramValue", ConditionTestSupport.setOf(19, 21, 22, 38, 40, 41)),
                Arguments.of("{}", ConditionTestSupport.setOf(20, 21, 23, 38, 39, 40, 41, 42)),
                Arguments.of(
                        "Key",
                        ConditionTestSupport.setOf(
                                24, 68, 69, 80, 81, 98, 99, 101, 102, 103, 105, 106, 107, 109, 110, 111, 112, 113, 121)),
                Arguments.of(
                        "Value",
                        ConditionTestSupport.setOf(
                                19, 21, 22, 24, 38, 40, 41, 68, 80, 81, 98, 99, 101, 102, 103, 105, 106, 107, 109, 111, 112,
                                113, 121)),
                Arguments.of(
                        "java.lang.RuntimeException", ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30)),
                Arguments.of("java.lang.NullPointerException", ConditionTestSupport.setOf(26, 27, 28, 29, 30)),
                Arguments.of("java.lang.FooException", ConditionTestSupport.setOf(27, 29, 30)),
                Arguments.of("java.lang.BarException", ConditionTestSupport.setOf(30)),
                Arguments.of("RuntimeException", ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30)),
                Arguments.of("Exception", ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30)),
                Arguments.of("java.lang", ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30)),
                Arguments.of("-Marker", ConditionTestSupport.setOf(31, 32, 88)),
                Arguments.of("Foo-Marker", ConditionTestSupport.setOf(31)),
                Arguments.of("Bar-Marker", ConditionTestSupport.setOf(31, 32)),
                Arguments.of("Context", ConditionTestSupport.setOf(76, 77, 80, 81)),
                Arguments.of("threadName", ConditionTestSupport.setOf(83)),
                Arguments.of("11337", ConditionTestSupport.setOf(84)),
                Arguments.of("groupName", ConditionTestSupport.setOf(85)),
                Arguments.of("31337", ConditionTestSupport.setOf(86)),
                Arguments.of("1337", ConditionTestSupport.setOf(84, 86)),
                Arguments.of("Name", ConditionTestSupport.setOf(76, 77, 83, 85)),
                Arguments.of("b0rked3", ConditionTestSupport.setOf(87)),
                Arguments.of("exception1", ConditionTestSupport.setOf(89)),
                Arguments.of("exception2", ConditionTestSupport.setOf(90)),
                Arguments.of("exception3", ConditionTestSupport.setOf(91)),
                Arguments.of("exception4", ConditionTestSupport.setOf(92)),
                Arguments.of("exception5", ConditionTestSupport.setOf(92)),
                Arguments.of("exception", ConditionTestSupport.setOf(89, 90, 91, 92)),
                Arguments.of("404", ConditionTestSupport.setOf(56)),
                Arguments.of("50", ConditionTestSupport.setOf(58)),
                Arguments.of("setPressed", ConditionTestSupport.setOf(44, 45, 46, 47, 48, 49, 50, 51, 65, 66, 95, 96, 116)),
                Arguments.of("7", ConditionTestSupport.setOf(84, 86, 117)));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        EventContainsCondition condition = new EventContainsCondition();
        condition.setSearchString(searchString);

        EventContainsCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        EventContainsCondition condition = new EventContainsCondition();
        condition.setSearchString(searchString);

        EventContainsCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        EventContainsCondition condition = new EventContainsCondition();
        condition.setSearchString(searchString);

        EventContainsCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", "value");
    }

    @Test
    void equalsBehavesAsExpected() {
        EventContainsCondition instance = new EventContainsCondition();
        EventContainsCondition other = new EventContainsCondition("foo");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
