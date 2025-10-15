/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

class ExcludeHttpRequestUriMenuTest extends FocusHttpRequestUriMenuTest {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new ExcludeHttpRequestUriMenu();
    }
}
