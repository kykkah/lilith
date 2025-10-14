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

class LoggingLoggerNameFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected LoggingLoggerNameFormatter createInstance() {
        return new LoggingLoggerNameFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(13, 14);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of("com.foo.Foo", "com.foo.Bar");
    }

    @Override
    protected boolean expectedAcceleratorAvailability() {
        return true;
    }
}
