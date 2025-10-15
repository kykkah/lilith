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

import de.huxhorn.sulky.conditions.Condition;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static de.huxhorn.sulky.junit.JUnitTools.testClone;
import static de.huxhorn.sulky.junit.JUnitTools.testSerialization;
import static de.huxhorn.sulky.junit.JUnitTools.testXmlSerialization;

final class ConditionTestSupport {

    private ConditionTestSupport() {}

    @SuppressWarnings("unchecked")
    static <T extends Condition> T serialize(T condition) {
        try {
            return (T) testSerialization(condition);
        } catch (Exception e) {
            throw new AssertionError("Serialization round-trip failed.", e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Condition> T xmlSerialize(T condition) {
        try {
            return (T) testXmlSerialization(condition);
        } catch (Exception e) {
            throw new AssertionError("XML serialization round-trip failed.", e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Condition> T cloneCondition(T condition) {
        try {
            return (T) testClone(condition);
        } catch (Exception e) {
            throw new AssertionError("Cloning failed.", e);
        }
    }

    static Set<Integer> setOf(int... values) {
        Set<Integer> result = new HashSet<>();
        for (int value : values) {
            result.add(value);
        }
        return result;
    }

    static Set<Integer> allIndices(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size must be >= 0");
        }
        Set<Integer> result = new HashSet<>();
        IntStream.range(0, size).forEach(result::add);
        return result;
    }
}
