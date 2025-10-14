package de.huxhorn.lilith.services.clipboard;

import static org.junit.jupiter.api.Assertions.assertNull;

import de.huxhorn.lilith.data.EventWrapperCorpus;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LoggingEventXmlFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected LoggingEventXmlFormatter createInstance() {
        return new LoggingEventXmlFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return EventWrapperCorpus.matchAnyLoggingEventSet();
    }

    @Override
    protected Set<Integer> excludedIndices() {
        Set<Integer> result = new HashSet<>(EventWrapperCorpus.matchAnyLoggingEventSet());
        result.removeAll(Set.of(13, 14, 114));
        return result;
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "<?xml version=\'1.0\' encoding=\'UTF-8\'?><LoggingEvent xmlns=\"http://lilith.sf.net/schema/logging/16\" logger=\"com.foo.Foo\" level=\"null\"/>",
                "<?xml version=\'1.0\' encoding=\'UTF-8\'?><LoggingEvent xmlns=\"http://lilith.sf.net/schema/logging/16\" logger=\"com.foo.Bar\" level=\"null\"/>",
                "<?xml version=\'1.0\' encoding=\'UTF-8\'?><LoggingEvent xmlns=\"http://lilith.sf.net/schema/logging/16\" logger=\"\" level=\"null\"/>");
    }

    @Test
    void explodingFormatterReturnsNull() {
        @SuppressWarnings("unchecked")
        List<EventWrapper<?>> corpus = (List<EventWrapper<?>>) (List<?>) EventWrapperCorpus.createCorpus();
        ClipboardFormatter formatter = createInstance();
        EventWrapper<?> exploding = corpus.get(6);
        assertNull(formatter.toString(exploding));
    }
}
