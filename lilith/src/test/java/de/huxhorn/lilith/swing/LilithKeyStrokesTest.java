/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing;

import de.huxhorn.sulky.swing.KeyStrokes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.KeyStroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LilithKeyStrokesTest {

    @BeforeAll
    static void setHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @ParameterizedTest(name = "keystroke for {0} round-trips")
    @MethodSource("sortedActionNames")
    void keystrokeRoundTrips(String actionName) {
        KeyStroke keyStroke = LilithKeyStrokes.getKeyStroke(actionName);
        assertNotNull(keyStroke);
        assertEquals(actionName, LilithKeyStrokes.getActionName(keyStroke));
    }

    @ParameterizedTest(name = "unprocessed key stroke for {0}")
    @MethodSource("sortedActionNames")
    void unprocessedKeyStrokeStringResolves(String actionName) {
        String unprocessedKeyStrokeString = LilithKeyStrokes.getUnprocessedKeyStrokeString(actionName);
        assertNotNull(unprocessedKeyStrokeString);
        assertNotNull(KeyStrokes.resolveAcceleratorKeyStroke(unprocessedKeyStrokeString));
    }

    @Test
    void addKeyStrokeWithEmptyStringThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> LilithKeyStrokes.addKeyStroke("", "foo"));
        assertEquals("keyStrokeString '' did not resolve to a KeyStroke!", exception.getMessage());
    }

    @Test
    void addKeyStrokeWithNullStringThrows() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> LilithKeyStrokes.addKeyStroke(null, "foo"));
        assertEquals("keyStrokeString must not be null!", exception.getMessage());
    }

    @Test
    void addKeyStrokeWithNullActionThrows() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> LilithKeyStrokes.addKeyStroke("foo", null));
        assertEquals("actionName must not be null!", exception.getMessage());
    }

    @Test
    void addKeyStrokeWithDuplicateKeyStrokeThrows() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> LilithKeyStrokes.addKeyStroke("command F", "foo"));
        assertEquals("Duplicate action name entry for 'command F': 'FIND' and 'foo'", exception.getMessage());
    }

    @Test
    void addKeyStrokeWithDuplicateActionNameThrows() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> LilithKeyStrokes.addKeyStroke("command F2", "FIND"));
        assertEquals("Duplicate action name entry 'FIND'!", exception.getMessage());
    }

    private static Stream<String> sortedActionNames() {
        List<String> actionNames = new ArrayList<>(LilithKeyStrokes.getActionNames());
        Collections.sort(actionNames);
        return actionNames.stream();
    }
}
