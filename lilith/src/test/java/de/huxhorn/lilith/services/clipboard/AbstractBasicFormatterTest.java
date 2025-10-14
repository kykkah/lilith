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

package de.huxhorn.lilith.services.clipboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.huxhorn.lilith.services.BasicFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

abstract class AbstractBasicFormatterTest {

    @BeforeAll
    static void configureHeadless() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void worksOnCorpus() {
        BasicFormatter instance = createInstance();
        Set<Integer> expectedIndices = new TreeSet<>(expectedIndices());
        Set<Integer> excludedIndices = new TreeSet<>(excludedIndices());
        List<String> expectedResults = new ArrayList<>(expectedResults());

        Set<Integer> compatibleIndices = new TreeSet<>(BasicFormatterCorpus.isCompatible(instance));
        assertEquals(expectedIndices, compatibleIndices);

        List<String> results = new ArrayList<>(BasicFormatterCorpus.toString(instance, excludedIndices));
        for (int i = 0; i < results.size(); i++) {
            if (excludedIndices.contains(i)) {
                continue;
            }
            if (compatibleIndices.contains(i)) {
                assertNotNull(results.get(i), "Expected non-null result at index " + i);
            } else {
                assertNull(results.get(i), "Expected null result at index " + i);
            }
        }

        results.removeIf(Objects::isNull);
        assertEquals(expectedResults, results);
        assertEquals(
                expectedIndices.size() - excludedIndices.size(),
                results.size(),
                "Sanity check: result count mismatch.");
    }

    protected abstract BasicFormatter createInstance();

    protected abstract Set<Integer> expectedIndices();

    protected abstract List<String> expectedResults();

    protected Set<Integer> excludedIndices() {
        return Collections.emptySet();
    }
}
