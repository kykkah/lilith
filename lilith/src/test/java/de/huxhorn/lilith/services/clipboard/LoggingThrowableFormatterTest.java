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

class LoggingThrowableFormatterTest extends AbstractClipboardFormatterTest {

    private static final String NL = System.lineSeparator();

    @Override
    protected LoggingThrowableFormatter createInstance() {
        return new LoggingThrowableFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(25, 26, 27, 28, 29, 30, 89, 90, 91, 92, 93, 94, 95, 96, 115);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "java.lang.RuntimeException" + NL,
                "java.lang.RuntimeException" + NL + "Caused by: java.lang.NullPointerException" + NL,
                "java.lang.RuntimeException" + NL
                        + "Caused by: java.lang.NullPointerException" + NL
                        + "Caused by: java.lang.FooException" + NL,
                "java.lang.RuntimeException" + NL + "\tSuppressed: java.lang.NullPointerException" + NL,
                "java.lang.RuntimeException" + NL
                        + "\tSuppressed: java.lang.NullPointerException" + NL
                        + "\tSuppressed: java.lang.FooException" + NL,
                "java.lang.RuntimeException" + NL
                        + "\tSuppressed: java.lang.NullPointerException" + NL
                        + "\tSuppressed: java.lang.FooException" + NL
                        + "Caused by: java.lang.BarException" + NL,
                "exception1" + NL,
                "null" + NL + "Caused by: exception2" + NL,
                "null" + NL + "\tSuppressed: exception3" + NL,
                "null" + NL + "\tSuppressed: exception4" + NL + "\tSuppressed: exception5" + NL,
                "recursiveCause" + NL + "Caused by: recursiveCause[CIRCULAR REFERENCE]" + NL,
                "recursiveSuppressed" + NL + "\tSuppressed: recursiveSuppressed[CIRCULAR REFERENCE]" + NL,
                "null" + NL
                        + "\tat javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]" + NL
                        + "\tat javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348) ~[na:1.8.0_92]" + NL
                        + "\tat javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]" + NL
                        + "\tat javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]" + NL,
                "null" + NL
                        + "Caused by: null" + NL
                        + "\tat javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022) ~[na:1.8.0_92]" + NL
                        + "\tat javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402) ~[na:1.8.0_92]" + NL
                        + "\tat javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259) ~[na:1.8.0_92]" + NL,
                NL);
    }

    @Override
    protected boolean expectedAcceleratorAvailability() {
        return true;
    }
}
