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

import de.huxhorn.lilith.data.logging.ThrowableInfo;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThrowableConditionTest {

    @ParameterizedTest(name = "info={1}")
    @MethodSource("collectThrowableNamesCases")
    void collectThrowableNamesWorks(Set<String> expected, ThrowableInfo info) {
        Set<String> result = ThrowableCondition.collectThrowableNames(info);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> collectThrowableNamesCases() {
        return Stream.of(
                Arguments.of(setOfStrings(), null),
                Arguments.of(setOfStrings("java.lang.RuntimeException"), info("java.lang.RuntimeException")),
                Arguments.of(
                        setOfStrings("java.lang.RuntimeException", "java.lang.NullPointerException"),
                        withCause(info("java.lang.RuntimeException"), info("java.lang.NullPointerException"))),
                Arguments.of(
                        setOfStrings("java.lang.RuntimeException", "java.lang.NullPointerException", "java.lang.FooException"),
                        withCause(
                                info("java.lang.RuntimeException"),
                                withCause(info("java.lang.NullPointerException"), info("java.lang.FooException")))),
                Arguments.of(
                        setOfStrings("java.lang.RuntimeException", "java.lang.NullPointerException"),
                        withSuppressed(info("java.lang.RuntimeException"), info("java.lang.NullPointerException"))),
                Arguments.of(
                        setOfStrings("java.lang.RuntimeException", "java.lang.NullPointerException", "java.lang.FooException"),
                        withSuppressed(
                                info("java.lang.RuntimeException"),
                                info("java.lang.NullPointerException"),
                                info("java.lang.FooException"))),
                Arguments.of(
                        setOfStrings(
                                "java.lang.RuntimeException",
                                "java.lang.NullPointerException",
                                "java.lang.FooException",
                                "java.lang.BarException"),
                        withSuppressed(
                                withCause(info("java.lang.RuntimeException"), info("java.lang.BarException")),
                                info("java.lang.NullPointerException"),
                                info("java.lang.FooException"))),
                Arguments.of(
                        setOfStrings("java.lang.RuntimeException", "java.lang.NullPointerException", "java.lang.FooException"),
                        withSuppressed(
                                withCause(info("java.lang.RuntimeException"), new ThrowableInfo()),
                                info("java.lang.NullPointerException"),
                                info("java.lang.FooException"))));
    }

    private static Set<String> setOfStrings(String... values) {
        Set<String> result = new HashSet<>();
        for (String value : values) {
            result.add(value);
        }
        return result;
    }

    private static ThrowableInfo info(String name) {
        ThrowableInfo result = new ThrowableInfo();
        result.setName(name);
        return result;
    }

    private static ThrowableInfo withCause(ThrowableInfo root, ThrowableInfo cause) {
        root.setCause(cause);
        return root;
    }

    private static ThrowableInfo withSuppressed(ThrowableInfo root, ThrowableInfo... suppressed) {
        root.setSuppressed(suppressed);
        return root;
    }

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        ThrowableCondition condition = new ThrowableCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(
                        null,
                        ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30, 89, 90, 91, 92, 93, 94, 95, 96, 115)),
                Arguments.of(
                        "",
                        ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30, 89, 90, 91, 92, 93, 94, 95, 96, 115)),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("java.lang.RuntimeException", ConditionTestSupport.setOf(25, 26, 27, 28, 29, 30)),
                Arguments.of("java.lang.NullPointerException", ConditionTestSupport.setOf(26, 27, 28, 29, 30)),
                Arguments.of("java.lang.FooException", ConditionTestSupport.setOf(27, 29, 30)),
                Arguments.of("java.lang.BarException", ConditionTestSupport.setOf(30)));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        ThrowableCondition condition = new ThrowableCondition();
        condition.setSearchString(searchString);

        ThrowableCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        ThrowableCondition condition = new ThrowableCondition();
        condition.setSearchString(searchString);

        ThrowableCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        ThrowableCondition condition = new ThrowableCondition();
        condition.setSearchString(searchString);

        ThrowableCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", "value");
    }

    @Test
    void equalsBehavesAsExpected() {
        ThrowableCondition instance = new ThrowableCondition();
        ThrowableCondition other = new ThrowableCondition("foo");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
