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

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.services.BasicFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class BasicFormatterCorpus {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFormatterCorpus.class);

    private BasicFormatterCorpus() {}

    static List<Object> createCorpus() {
        return EventWrapperCorpus.createCorpus();
    }

    static Set<Integer> isCompatible(BasicFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter must not be null!");
        List<Object> corpus = createCorpus();
        Set<Integer> result = new TreeSet<>();
        for (int i = 0; i < corpus.size(); i++) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Before isCompatible(corpus[{}])...", i);
            }
            if (formatter.isCompatible(corpus.get(i))) {
                result.add(i);
            }
        }
        return result;
    }

    static List<String> toString(BasicFormatter formatter) {
        return toString(formatter, Collections.emptySet());
    }

    static List<String> toString(BasicFormatter formatter, Set<Integer> excluded) {
        Objects.requireNonNull(formatter, "formatter must not be null!");
        Objects.requireNonNull(excluded, "excluded must not be null!");

        List<Object> corpus = createCorpus();
        List<String> result = new ArrayList<>(corpus.size());
        for (int i = 0; i < corpus.size(); i++) {
            if (excluded.contains(i)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Skipping excluded toString(corpus[{}])...", i);
                }
                result.add(null);
                continue;
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Before toString(corpus[{}])...", i);
            }
            result.add(formatter.toString(corpus.get(i)));
        }
        return result;
    }
}
