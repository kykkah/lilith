/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

class ExcludeNDCMenuTest extends FocusNDCMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new ExcludeNDCMenu(false);
    }
}
