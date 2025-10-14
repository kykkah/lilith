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

class HttpRemoteUserConditionTest {

    private static final Set<Integer> ACCESS_EVENTS_WITHOUT_REMOTE_USER = ConditionTestSupport.setOf(
            5,
            52,
            53,
            54,
            55,
            56,
            57,
            58,
            59,
            60,
            61,
            62,
            70,
            71,
            72,
            73,
            74,
            75,
            77,
            79,
            81,
            100,
            101,
            102,
            103,
            104,
            105,
            106,
            107,
            108,
            109,
            110,
            111,
            112,
            113,
            118,
            119,
            120,
            122);

    private static final Set<Integer> SFALKEN_EVENTS = ConditionTestSupport.setOf(63, 64);

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        HttpRemoteUserCondition condition = new HttpRemoteUserCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ACCESS_EVENTS_WITHOUT_REMOTE_USER),
                Arguments.of("", ACCESS_EVENTS_WITHOUT_REMOTE_USER),
                Arguments.of("-", ACCESS_EVENTS_WITHOUT_REMOTE_USER),
                Arguments.of("   ", ACCESS_EVENTS_WITHOUT_REMOTE_USER),
                Arguments.of(" - ", ACCESS_EVENTS_WITHOUT_REMOTE_USER),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("sfalken", SFALKEN_EVENTS),
                Arguments.of("   sfalken   ", SFALKEN_EVENTS));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        HttpRemoteUserCondition condition = new HttpRemoteUserCondition();
        condition.setSearchString(searchString);

        HttpRemoteUserCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getUserName(), result.getUserName());
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        HttpRemoteUserCondition condition = new HttpRemoteUserCondition();
        condition.setSearchString(searchString);

        HttpRemoteUserCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getUserName(), result.getUserName());
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        HttpRemoteUserCondition condition = new HttpRemoteUserCondition();
        condition.setSearchString(searchString);

        HttpRemoteUserCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getUserName(), result.getUserName());
    }

    private static Stream<String> inputValues() {
        return Stream.of(null, "", " ", "-", " - ", "sfalken", " sfalken ");
    }

    @Test
    void equalsBehavesAsExpected() {
        HttpRemoteUserCondition instance = new HttpRemoteUserCondition();
        HttpRemoteUserCondition other = new HttpRemoteUserCondition("foo");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }
}
