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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CallLocationConditionTest {

    @ParameterizedTest(name = "searchString={0}")
    @MethodSource("corpusCases")
    void corpusWorks(String searchString, Set<Integer> expected) {
        CallLocationCondition condition = new CallLocationCondition(searchString);

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(null, ConditionTestSupport.setOf()),
                Arguments.of("", ConditionTestSupport.setOf()),
                Arguments.of("snafu", ConditionTestSupport.setOf()),
                Arguments.of("actionPerformed(DebugDialog.java:358)", ConditionTestSupport.setOf()),
                Arguments.of(
                        "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358) "
                                + "[de.huxhorn.lilith-8.1.0-SNAPSHOT.jar:na]",
                        ConditionTestSupport.setOf(44, 45, 46, 47, 66)),
                Arguments.of(
                        "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358) "
                                + "~[de.huxhorn.lilith-8.1.0-SNAPSHOT.jar:na]",
                        ConditionTestSupport.setOf(44, 45, 46, 47, 66)),
                Arguments.of(
                        "de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)",
                        ConditionTestSupport.setOf(44, 45, 46, 47, 66)),
                Arguments.of(
                        "\t\tat de.huxhorn.lilith.debug.DebugDialog$LogAllAction.actionPerformed(DebugDialog.java:358)",
                        ConditionTestSupport.setOf(44, 45, 46, 47, 66)),
                Arguments.of(
                        "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) [na:1.8.0_92]",
                        ConditionTestSupport.setOf(48, 49, 50, 51)),
                Arguments.of(
                        "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]",
                        ConditionTestSupport.setOf(48, 49, 50, 51)),
                Arguments.of(
                        "javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)",
                        ConditionTestSupport.setOf(48, 49, 50, 51)),
                Arguments.of(
                        "    javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]   ",
                        ConditionTestSupport.setOf(48, 49, 50, 51)),
                Arguments.of(
                        "javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252) "
                                + "[na:1.8.0_92]",
                        ConditionTestSupport.setOf()),
                Arguments.of(
                        "javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252) "
                                + "~[na:1.8.0_92]",
                        ConditionTestSupport.setOf()),
                Arguments.of(
                        "javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252)",
                        ConditionTestSupport.setOf()),
                Arguments.of(
                        "java.awt.AWTEventMulticaster.mouseReleased(AWTEventMulticaster.java:289) [na:1.8.0_92]",
                        ConditionTestSupport.setOf()),
                Arguments.of(
                        "java.awt.AWTEventMulticaster.mouseReleased(AWTEventMulticaster.java:289) ~[na:1.8.0_92]",
                        ConditionTestSupport.setOf()),
                Arguments.of(
                        "java.awt.AWTEventMulticaster.mouseReleased(AWTEventMulticaster.java:289)",
                        ConditionTestSupport.setOf()));
    }

    @ParameterizedTest(name = "serialize search={0}")
    @MethodSource("inputValues")
    void serializationRoundTrip(String searchString) {
        CallLocationCondition condition = new CallLocationCondition();
        condition.setSearchString(searchString);

        CallLocationCondition result = ConditionTestSupport.serialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStackTraceElement(), result.getStackTraceElement());
        if (condition.getStackTraceElement() != null) {
            assertNotSame(condition.getStackTraceElement(), result.getStackTraceElement());
        }
    }

    @ParameterizedTest(name = "xml serialize search={0}")
    @MethodSource("inputValues")
    void xmlSerializationRoundTrip(String searchString) {
        CallLocationCondition condition = new CallLocationCondition();
        condition.setSearchString(searchString);

        CallLocationCondition result = ConditionTestSupport.xmlSerialize(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStackTraceElement(), result.getStackTraceElement());
        if (condition.getStackTraceElement() != null) {
            assertNotSame(condition.getStackTraceElement(), result.getStackTraceElement());
        }
    }

    @ParameterizedTest(name = "clone search={0}")
    @MethodSource("inputValues")
    void cloningRetainsSearchString(String searchString) {
        CallLocationCondition condition = new CallLocationCondition();
        condition.setSearchString(searchString);

        CallLocationCondition result = ConditionTestSupport.cloneCondition(condition);

        assertEquals(searchString, result.getSearchString());
        assertEquals(condition.getStackTraceElement(), result.getStackTraceElement());
        if (condition.getStackTraceElement() != null) {
            assertNotSame(condition.getStackTraceElement(), result.getStackTraceElement());
        }
    }

    private static Stream<String> inputValues() {
        return Stream.of(
                null,
                "",
                "value",
                "javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252) ~[na:1.8.0_92]",
                "javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252)");
    }

    @Test
    void equalsBehavesAsExpected() {
        CallLocationCondition instance = new CallLocationCondition();
        CallLocationCondition other = new CallLocationCondition(
                "javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252)");

        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
        assertNotEquals(instance, other);
        assertNotEquals(other, instance);
    }

    @Test
    void parseStackTraceElementNullReturnsNull() {
        assertNull(CallLocationCondition.parseStackTraceElement(null));
    }
}
