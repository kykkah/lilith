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

import de.huxhorn.sulky.junit.JUnitTools;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GroovyConditionTest {

    @ParameterizedTest(name = "script={0}, searchString={1}")
    @MethodSource("corpusCases")
    void corpusWorks(String script, String searchString, Set<Integer> expected, @TempDir Path tempDir)
            throws IOException {
        File groovyFile = tempDir.resolve(script + ".groovy").toFile();
        JUnitTools.copyResourceToFile("/" + script + ".txt", groovyFile);

        GroovyCondition condition = new GroovyCondition(groovyFile.getAbsolutePath(), searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of("WorkingCondition", null, ConditionTestSupport.setOf(67, 97)),
                Arguments.of("WorkingScript", null, ConditionTestSupport.setOf(67, 97)),
                Arguments.of("WorkingScriptReturnOther", null, ConditionTestSupport.setOf(67, 97)),
                Arguments.of("WorkingScriptThrowing", null, ConditionTestSupport.setOf(67, 97)),
                Arguments.of(
                        "WorkingScriptWithSearchString",
                        null,
                        ConditionTestSupport.setOf(
                                6, 7, 8, 9, 10, 11, 12, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
                                32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 65, 66, 67,
                                68, 69, 76, 78, 80, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
                                115, 116, 117, 121)),
                Arguments.of("WorkingScriptWithSearchString", "com.foo.Foo", ConditionTestSupport.setOf(13)),
                Arguments.of("SyntaxError", null, ConditionTestSupport.setOf()),
                Arguments.of("WrongType", null, ConditionTestSupport.setOf()));
    }

    @ParameterizedTest(name = "serialize script={0}, searchString={1}")
    @MethodSource("scriptAndSearchValues")
    void serializationRoundTrip(String scriptFileName, String searchString) {
        GroovyCondition condition = new GroovyCondition();
        condition.setScriptFileName(scriptFileName);
        condition.setSearchString(searchString);

        GroovyCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(scriptFileName, result.getScriptFileName());
        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "xml serialize script={0}, searchString={1}")
    @MethodSource("scriptAndSearchValues")
    void xmlSerializationRoundTrip(String scriptFileName, String searchString) {
        GroovyCondition condition = new GroovyCondition();
        condition.setScriptFileName(scriptFileName);
        condition.setSearchString(searchString);

        GroovyCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(scriptFileName, result.getScriptFileName());
        assertEquals(searchString, result.getSearchString());
    }

    @ParameterizedTest(name = "clone script={0}, searchString={1}")
    @MethodSource("scriptAndSearchValues")
    void cloningRetainsFields(String scriptFileName, String searchString) {
        GroovyCondition condition = new GroovyCondition();
        condition.setScriptFileName(scriptFileName);
        condition.setSearchString(searchString);

        GroovyCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(scriptFileName, result.getScriptFileName());
        assertEquals(searchString, result.getSearchString());
    }

    private static Stream<Arguments> scriptAndSearchValues() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, "foo"),
                Arguments.of(null, ""),
                Arguments.of("", null),
                Arguments.of("", "foo"),
                Arguments.of("", ""),
                Arguments.of("file", null),
                Arguments.of("file", "foo"),
                Arguments.of("file", ""));
    }

    @ParameterizedTest(name = "instance={0}, other={1}")
    @MethodSource("equalsCases")
    void equalsBehavesAsExpected(GroovyCondition instance, GroovyCondition other) {
        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }

    private static Stream<Arguments> equalsCases() {
        return Stream.of(
                Arguments.of(new GroovyCondition(), new GroovyCondition("script", "searchString")),
                Arguments.of(new GroovyCondition("script"), new GroovyCondition("script", "searchString")),
                Arguments.of(new GroovyCondition(null, "searchString"), new GroovyCondition("script", "searchString")));
    }
}
