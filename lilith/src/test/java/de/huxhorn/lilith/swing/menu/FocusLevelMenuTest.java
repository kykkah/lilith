/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.menu;

import de.huxhorn.lilith.data.EventWrapperCorpus;

import java.util.Set;

class FocusLevelMenuTest extends AbstractFilterMenuTestBase {

    @Override
    protected AbstractFilterMenu createMenu() {
        return new FocusLevelMenu();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return EventWrapperCorpus.matchAnyLoggingEventSet();
    }
}
