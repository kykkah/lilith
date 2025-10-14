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

import java.util.List;
import java.util.Set;

class LoggingThrowableNameFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected LoggingThrowableNameFormatter createInstance() {
        return new LoggingThrowableNameFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(25, 26, 27, 28, 29, 30, 93, 94);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "java.lang.RuntimeException",
                "recursiveCause",
                "recursiveSuppressed");
    }

    @Override
    protected boolean expectedAcceleratorAvailability() {
        return true;
    }
}
