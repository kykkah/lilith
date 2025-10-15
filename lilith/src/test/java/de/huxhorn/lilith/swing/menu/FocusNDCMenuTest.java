/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import java.util.Set;

class FocusNDCMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new FocusNDCMenu(false);
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(36, 37, 38, 39, 40, 41, 42, 87);
    }
}
