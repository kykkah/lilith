/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import java.util.Set;

class FocusMDCMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new FocusMDCMenu();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(24, 68, 69, 98, 121);
    }
}
