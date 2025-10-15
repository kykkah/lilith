/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardHelpTest {

    private static final String COMMAND_REPLACEMENT = "^";
    private static final String SHIFT_REPLACEMENT = "\u21E7";
    private static final String ALT_REPLACEMENT = "alt";
    private static final String ENTER_REPLACEMENT = "\u21B5";

    private static final Map<String, String> REPLACEMENTS = new LinkedHashMap<>();
    private static final Map<LilithActionId, String> ADDITIONAL_INFO = new EnumMap<>(LilithActionId.class);
    private static final Map<LilithActionId, String> ADDITIONAL_KEYSTROKE_INFO = new EnumMap<>(LilithActionId.class);

    static {
        REPLACEMENTS.put("command", COMMAND_REPLACEMENT);
        REPLACEMENTS.put("shift", SHIFT_REPLACEMENT);
        REPLACEMENTS.put("alt", ALT_REPLACEMENT);
        REPLACEMENTS.put("enter", ENTER_REPLACEMENT);
        REPLACEMENTS.put(" ", "+");

        ADDITIONAL_INFO.put(LilithActionId.EXIT,
                "No, I won't ask you for permission. I will only ask for permission if you make me do so in Preferences, though. :p");

        ADDITIONAL_KEYSTROKE_INFO.put(LilithActionId.ZOOM_IN,
                "(also: " + COMMAND_REPLACEMENT + "+[mouse wheel up])");
        ADDITIONAL_KEYSTROKE_INFO.put(LilithActionId.ZOOM_OUT,
                "(also: " + COMMAND_REPLACEMENT + "+[mouse wheel down])");
    }

    @Test
    void keyboardHelpIsUpToDate() throws IOException {
        String currentKeyboardHelpText = loadKeyboardHelp();

        StringBuilder builder = new StringBuilder();
        appendNotations(builder);
        appendCategories(builder);
        String generatedKeyboardHelp = builder.toString();

        String cleanedCurrent = removeSomeWhitespace(currentKeyboardHelpText);
        String cleanedGenerated = removeSomeWhitespace(generatedKeyboardHelp);

        assertTrue(cleanedCurrent.contains(cleanedGenerated),
                "Generated keyboard help does not match existing help file.");
    }

    private static String loadKeyboardHelp() throws IOException {
        try (InputStream in = KeyboardHelpTest.class.getResourceAsStream("/help/keyboard.xhtml")) {
            Objects.requireNonNull(in, "keyboard.xhtml resource not found");
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        }
    }

    private static String removeSomeWhitespace(String input) {
        return input.replaceAll("(?m)>\\s+<", "><").trim();
    }

    private static void appendNotations(StringBuilder builder) {
        builder.append("\n<h2>Notation</h2>\n<dl>\n");
        for (Notation notation : notation()) {
            builder.append("\t<dt>")
                    .append(toXHtml(notation.text))
                    .append("</dt>\n\t<dd>")
                    .append(toXHtml(notation.description))
                    .append("</dd>\n");
        }
        builder.append("</dl>\n");
    }

    private static void appendCategories(StringBuilder builder) {
        for (Category category : allCategories()) {
            appendCategory(builder, category);
        }
    }

    private static void appendCategory(StringBuilder builder, Category category) {
        List<LilithActionId> filteredActions = filterActionsWithKeystroke(category.entries);
        if (filteredActions.isEmpty()) {
            return;
        }

        builder.append("\n<h2>")
                .append(toXHtml(category.name))
                .append("</h2>\n<dl>\n");
        for (LilithActionId entry : filteredActions) {
            String keyStrokeString = LilithKeyStrokes.getUnprocessedKeyStrokeString(entry.name());
            keyStrokeString = processKeystrokeString(keyStrokeString);
            if (ADDITIONAL_KEYSTROKE_INFO.containsKey(entry)) {
                keyStrokeString = keyStrokeString + " " + ADDITIONAL_KEYSTROKE_INFO.get(entry);
            }

            String descriptionString = entry.getText();
            if (entry.getDescription() != null) {
                descriptionString = descriptionString + "\n" + entry.getDescription();
            }
            if (ADDITIONAL_INFO.containsKey(entry)) {
                descriptionString = descriptionString + "\n" + ADDITIONAL_INFO.get(entry);
            }

            builder.append("\t<dt>")
                    .append(toXHtml(keyStrokeString))
                    .append("</dt>\n\t<dd>")
                    .append(toXHtml(descriptionString))
                    .append("</dd>\n");
        }
        builder.append("</dl>\n");
    }

    private static String processKeystrokeString(String keystrokeString) {
        String result = keystrokeString;
        for (Map.Entry<String, String> entry : REPLACEMENTS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        if (result.contains("++")) {
            throw new IllegalArgumentException("KeystrokeString '" + keystrokeString + "' contained multiple consecutive spaces.");
        }
        return result;
    }

    private static String toXHtml(String input) {
        return StringEscapeUtils.escapeXml10(input).replace("\n", "<br/>");
    }

    private static List<LilithActionId> filterActionsWithKeystroke(List<LilithActionId> actions) {
        return actions.stream()
                .filter(action -> LilithKeyStrokes.getKeyStroke(action) != null)
                .collect(Collectors.toList());
    }

    private static List<Category> allCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(LilithActionId.FILE.getText(), LilithActionGroups.fileMenuActions()));
        categories.add(new Category(LilithActionId.EDIT.getText(), LilithActionGroups.editMenuActions()));
        categories.add(new Category(LilithActionId.SEARCH.getText(), LilithActionGroups.searchMenuActions()));
        categories.add(new Category(LilithActionId.VIEW.getText(), LilithActionGroups.viewMenuActions()));
        categories.add(new Category(LilithActionId.WINDOW.getText(), LilithActionGroups.windowMenuActions()));
        categories.add(new Category(LilithActionId.HELP.getText(), LilithActionGroups.helpMenuActions()));
        return categories;
    }

    private static List<Notation> notation() {
        List<Notation> notations = new ArrayList<>();
        notations.add(new Notation(COMMAND_REPLACEMENT,
                "represents the system dependent command key, e.g. \"Ctrl\" on Windows and \"cmd \u2318\" on macOS."));
        notations.add(new Notation(ALT_REPLACEMENT, "represents \"Alt\" (\"alt \u2325\" on Mac)."));
        notations.add(new Notation(SHIFT_REPLACEMENT, "represents \"Shift\"."));
        notations.add(new Notation(ENTER_REPLACEMENT, "represents \"Enter\" or \"Return\"."));
        return notations;
    }

    private record Category(String name, List<LilithActionId> entries) {}

    private record Notation(String text, String description) {}
}
