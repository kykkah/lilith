/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

class ExcludeMDCMenuTest extends FocusMDCMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new ExcludeMDCMenu();
    }
}
