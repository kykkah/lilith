/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.preferences;

import de.huxhorn.lilith.swing.LilithActionId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TroubleshootingPanelTest {

    @Test
    void resolveMessageContainsExpectedInformation() {
        String message = TroubleshootingPanel.resolveMessage();

        assertNotNull(message);
        assertFalse(message.contains("#"));
        assertTrue(message.contains(LilithActionId.WINDOW.getText()));
        assertTrue(message.contains(LilithActionId.VIEW_LILITH_LOGS.getText()));
    }
}
