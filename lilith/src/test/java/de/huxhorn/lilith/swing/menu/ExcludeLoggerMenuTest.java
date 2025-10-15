/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

class ExcludeLoggerMenuTest extends FocusLoggerMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new ExcludeLoggerMenu();
    }
}
