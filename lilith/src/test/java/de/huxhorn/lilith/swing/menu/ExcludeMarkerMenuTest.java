/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

class ExcludeMarkerMenuTest extends FocusMarkerMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new ExcludeMarkerMenu();
    }
}
