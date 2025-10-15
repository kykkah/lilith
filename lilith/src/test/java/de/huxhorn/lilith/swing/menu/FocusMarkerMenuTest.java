/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import java.util.Set;

class FocusMarkerMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new FocusMarkerMenu();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(31, 32, 88);
    }
}
