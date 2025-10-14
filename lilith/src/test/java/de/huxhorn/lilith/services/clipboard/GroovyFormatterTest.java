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

import de.huxhorn.sulky.junit.JUnitTools;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GroovyFormatterTest extends AbstractClipboardFormatterTest {

    @TempDir
    Path tempDir;

    @Override
    protected GroovyFormatter createInstance() {
        File script = copyScript("CopyMdcValue");
        return new GroovyFormatter(script.getAbsolutePath());
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(24, 68);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of("mdcValue", "otherMdcValue");
    }

    @Override
    protected boolean expectedNative() {
        return false;
    }

    @Override
    protected boolean expectedAcceleratorAvailability() {
        return true;
    }

    @Test
    void wrongTypeOnCorpus() {
        File script = copyScript("WrongType");
        GroovyFormatter formatter = new GroovyFormatter(script.getAbsolutePath());

        Set<Integer> compatibleIndices = BasicFormatterCorpus.isCompatible(formatter);
        List<String> results = BasicFormatterCorpus.toString(formatter, Collections.emptySet());

        assertEquals(Collections.emptySet(), compatibleIndices);
        results.removeIf(item -> item == null);
        assertEquals(Collections.emptyList(), results);
    }

    @Test
    void wrongTypeAttributes() {
        File script = copyScript("WrongType");
        GroovyFormatter formatter = new GroovyFormatter(script.getAbsolutePath());

        assertEquals("WrongType.groovy", formatter.getName());
        assertEquals(
                "WrongType.groovy - Expected ClipboardFormatter but received WrongType!",
                formatter.getDescription());
        assertNull(formatter.getAccelerator());
    }

    @Test
    void syntaxErrorAttributes() {
        File script = copyScript("SyntaxError");
        GroovyFormatter formatter = new GroovyFormatter(script.getAbsolutePath());

        assertEquals("SyntaxError.groovy", formatter.getName());
        assertEquals(
                "SyntaxError.groovy - Exception while parsing class from '"
                        + script.getAbsolutePath()
                        + "'!",
                formatter.getDescription());
        assertNull(formatter.getAccelerator());
    }

    @Test
    void workingAttributes() {
        GroovyFormatter formatter = createInstance();

        assertEquals("Copy mdcKey value", formatter.getName());
        assertEquals("Copy mdcKey value from MDC, if available.", formatter.getDescription());
        assertNotNull(formatter.getAccelerator());
    }

    @Test
    void missingFileAttributes() {
        File missingFile = tempDir.resolve("MissingFile.groovy").toFile();
        GroovyFormatter formatter = new GroovyFormatter(missingFile.getAbsolutePath());

        assertEquals("MissingFile.groovy", formatter.getName());
        assertEquals(
                "MissingFile.groovy - '" + missingFile.getAbsolutePath() + "' is not a file!",
                formatter.getDescription());
        assertNull(formatter.getAccelerator());
    }

    @Test
    void nullFileAttributes() {
        GroovyFormatter formatter = new GroovyFormatter();

        assertEquals("Missing file!", formatter.getName());
        assertEquals("Missing file! - groovyFileName must not be null!", formatter.getDescription());
        assertNull(formatter.getAccelerator());
    }

    private File copyScript(String baseName) {
        File script = tempDir.resolve(baseName + ".groovy").toFile();
        try {
            JUnitTools.copyResourceToFile("/" + baseName + ".txt", script);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy script " + baseName, e);
        }
        return script;
    }
}
