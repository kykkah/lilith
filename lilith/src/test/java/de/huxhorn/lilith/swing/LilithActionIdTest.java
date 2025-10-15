/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LilithActionIdTest {

    @ParameterizedTest(name = "{0} has text")
    @MethodSource("allActionIds")
    void actionHasText(LilithActionId actionId) {
        assertNotNull(actionId.getText());
    }

    @ParameterizedTest(name = "{0} has mnemonic")
    @MethodSource("allActionIds")
    void actionHasMnemonic(LilithActionId actionId) {
        assertNotNull(actionId.getMnemonic());
    }

    @ParameterizedTest(name = "{0} contains mnemonic in text")
    @MethodSource("allActionIds")
    void mnemonicCharacterPresentInText(LilithActionId actionId) {
        Integer mnemonic = actionId.getMnemonic();
        String text = actionId.getText();
        String mnemonicChar = Character.toString((char) mnemonic.intValue());

        assertTrue(text.toLowerCase(Locale.US).contains(mnemonicChar));
    }

    @ParameterizedTest(name = "{0} text differs from description")
    @MethodSource("allActionIds")
    void textDoesNotEqualDescription(LilithActionId actionId) {
        String text = actionId.getText();
        String description = actionId.getDescription();
        assertTrue(text == null || !text.equals(description));
    }

    @ParameterizedTest(name = "{0} has distinct mnemonics")
    @MethodSource("actionGroups")
    void actionGroupHasDistinctMnemonics(List<LilithActionId> actionGroup) {
        Map<Integer, LilithActionId> mapping = new HashMap<>();
        Map<LilithActionId, Integer> collisions = new HashMap<>();

        for (LilithActionId actionId : actionGroup) {
            Integer mnemonic = actionId.getMnemonic();
            if (mnemonic != null) {
                LilithActionId previous = mapping.put(mnemonic, actionId);
                if (previous != null) {
                    collisions.put(previous, mnemonic);
                    collisions.put(actionId, mnemonic);
                }
            }
        }

        assertTrue(collisions.isEmpty(), () -> "Collisions detected: " + collisions);
    }

    @Test
    void allActionsAreAccountedFor() {
        Set<LilithActionId> remaining = new java.util.LinkedHashSet<>();
        Collections.addAll(remaining, LilithActionId.values());
        remaining.removeAll(LilithActionGroups.combinedActions());
        assertTrue(remaining.isEmpty(), () -> "Unassigned actions: " + remaining);
    }

    private static Stream<LilithActionId> allActionIds() {
        return Stream.of(LilithActionId.values());
    }

    private static Stream<List<LilithActionId>> actionGroups() {
        return LilithActionGroups.combinedActionGroups().stream();
    }
}
