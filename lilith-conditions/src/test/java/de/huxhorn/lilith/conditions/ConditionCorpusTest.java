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
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.huxhorn.sulky.conditions.Condition;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ConditionCorpusTest {

    @ParameterizedTest
    @MethodSource("corpusCases")
    void corpusExecutionMatchesExpectations(Condition condition, Set<Integer> expected) {
        List<Object> corpus = ConditionCorpus.createCorpus();

        Set<Integer> result = ConditionCorpus.executeConditionOnCorpus(condition, corpus);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> corpusCases() {
        return Stream.of(
                Arguments.of(new MatchNoneCondition(), ConditionTestSupport.setOf()),
                Arguments.of(new MatchAllCondition(), ConditionCorpus.matchAllSet()));
    }

    @Test
    void executeConditionOnCorpusNullConditionThrows() {
        NullPointerException ex =
                assertThrows(NullPointerException.class, () -> ConditionCorpus.executeConditionOnCorpus(null));
        assertEquals("condition must not be null!", ex.getMessage());
    }

    @Test
    void executeConditionOnCorpusNullConditionWithNullCorpusThrows() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class, () -> ConditionCorpus.executeConditionOnCorpus(null, null));
        assertEquals("condition must not be null!", ex.getMessage());
    }

    @Test
    void executeConditionOnCorpusNullConditionWithEmptyCorpusThrows() {
        NullPointerException ex =
                assertThrows(
                        NullPointerException.class, () -> ConditionCorpus.executeConditionOnCorpus(null, List.of()));
        assertEquals("condition must not be null!", ex.getMessage());
    }

    @Test
    void executeConditionOnCorpusNullCorpusThrows() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> ConditionCorpus.executeConditionOnCorpus(new MatchNoneCondition(), null));
        assertEquals("corpus must not be null!", ex.getMessage());
    }

    @Test
    void matchAllSetReturnsExpectedIndices() {
        List<Object> corpus = ConditionCorpus.createCorpus();
        Set<Integer> expected = ConditionTestSupport.allIndices(corpus.size());

        assertEquals(expected, ConditionCorpus.matchAllSet());
    }

    private static final class MatchNoneCondition implements Condition {

        @Override
        public boolean isTrue(Object element) {
            return false;
        }

        @Override
        public Condition clone() throws CloneNotSupportedException {
            return (Condition) super.clone();
        }

        @Override
        public String toString() {
            return "MatchNone";
        }
    }

    private static final class MatchAllCondition implements Condition {

        @Override
        public boolean isTrue(Object element) {
            return true;
        }

        @Override
        public Condition clone() throws CloneNotSupportedException {
            return (Condition) super.clone();
        }

        @Override
        public String toString() {
            return "MatchAll";
        }
    }
}
