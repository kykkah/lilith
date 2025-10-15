/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

class ExcludeHttpStatusTypeMenuTest extends FocusHttpStatusTypeMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new ExcludeHttpStatusTypeMenu();
    }
}
