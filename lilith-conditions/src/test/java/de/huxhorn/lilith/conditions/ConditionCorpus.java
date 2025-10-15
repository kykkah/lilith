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

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.sulky.conditions.Condition;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConditionCorpus {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionCorpus.class);

    private static final Set<Integer> MATCH_ALL_SET =
            Collections.unmodifiableSet(EventWrapperCorpus.matchAllSet(createCorpus()));

    private ConditionCorpus() {}

    public static List<Object> createCorpus() {
        return EventWrapperCorpus.createCorpus();
    }

    public static Set<Integer> matchAllSet() {
        return MATCH_ALL_SET;
    }

    public static Set<Integer> executeConditionOnCorpus(Condition condition) {
        return executeConditionOnCorpus(condition, createCorpus());
    }

    public static Set<Integer> executeConditionOnCorpus(Condition condition, List<Object> corpus) {
        Objects.requireNonNull(condition, "condition must not be null!");
        Objects.requireNonNull(corpus, "corpus must not be null!");

        Set<Integer> result = new TreeSet<>();
        for (int i = 0; i < corpus.size(); i++) {
            Object current = corpus.get(i);
            boolean conditionResult = condition.isTrue(current);
            if (conditionResult) {
                result.add(i);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                        "#{}: {}.isTrue({}) evaluated to {}.", i, condition, current, Boolean.valueOf(conditionResult));
            }
        }

        return result;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private static class SerializablePlaceholder implements Serializable {
        private static final long serialVersionUID = -5207922872610875882L;
    }
}
