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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MDCContainsConditionTest {

    @ParameterizedTest(name = "key={0}, value={1}")
    @MethodSource("corpusCases")
    void corpusWorks(String key, String value, Set<Integer> expected) {
        MDCContainsCondition condition = new MDCContainsCondition(key, value);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, null, ConditionTestSupport.setOf()),
                Arguments.of("mdcKey", null, ConditionTestSupport.setOf(24, 68, 69)),
                Arguments.of("mdcKey", "mdcValue", ConditionTestSupport.setOf(24)));
    }

    @ParameterizedTest(name = "serialize key={0}, value={1}")
    @MethodSource("keyValueCases")
    void serializationRoundTrip(String key, String value) {
        MDCContainsCondition condition = new MDCContainsCondition();
        condition.setKey(key);
        condition.setValue(value);

        MDCContainsCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(key, result.getKey());
        assertEquals(value, result.getValue());
    }

    @ParameterizedTest(name = "xml serialize key={0}, value={1}")
    @MethodSource("keyValueCases")
    void xmlSerializationRoundTrip(String key, String value) {
        MDCContainsCondition condition = new MDCContainsCondition();
        condition.setKey(key);
        condition.setValue(value);

        MDCContainsCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(key, result.getKey());
        assertEquals(value, result.getValue());
    }

    @ParameterizedTest(name = "clone key={0}, value={1}")
    @MethodSource("keyValueCases")
    void cloningRetainsFields(String key, String value) {
        MDCContainsCondition condition = new MDCContainsCondition();
        condition.setKey(key);
        condition.setValue(value);

        MDCContainsCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(key, result.getKey());
        assertEquals(value, result.getValue());
    }

    private static Stream<Arguments> keyValueCases() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("key", "value"),
                Arguments.of("key", null),
                Arguments.of(null, "value"));
    }

    @ParameterizedTest(name = "instance={0}, other={1}")
    @MethodSource("equalsCases")
    void equalsBehavesAsExpected(MDCContainsCondition instance, MDCContainsCondition other) {
        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }

    private static Stream<Arguments> equalsCases() {
        return Stream.of(
                Arguments.of(new MDCContainsCondition(), new MDCContainsCondition("", "")),
                Arguments.of(new MDCContainsCondition(), new MDCContainsCondition("key", "value")),
                Arguments.of(new MDCContainsCondition(), new MDCContainsCondition("key", null)),
                Arguments.of(new MDCContainsCondition(), new MDCContainsCondition(null, "value")));
    }
}
