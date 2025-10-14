package de.huxhorn.lilith.services.clipboard;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.swing.MainFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class EventHtmlFormatterTest extends AbstractClipboardFormatterTest {

    private static final String MOCKED = "MOCKED";

    @Override
    protected EventHtmlFormatter createInstance() {
        MainFrame mainFrame = mock(MainFrame.class);
        when(mainFrame.createMessage(any())).thenReturn(MOCKED);
        return new EventHtmlFormatter(mainFrame);
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return EventWrapperCorpus.matchAnyLoggingOrAccessEventSet();
    }

    @Override
    protected List<String> expectedResults() {
        Set<Integer> indices = expectedIndices();
        List<String> results = new ArrayList<>(indices.size());
        indices.forEach(index -> results.add(MOCKED));
        return results;
    }
}
