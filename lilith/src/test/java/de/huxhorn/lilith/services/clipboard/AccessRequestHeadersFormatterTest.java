package de.huxhorn.lilith.services.clipboard;

import java.util.List;
import java.util.Set;

class AccessRequestHeadersFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected AccessRequestHeadersFormatter createInstance() {
        return new AccessRequestHeadersFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(101, 102, 103);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "[\'requestHeaderKey\':\'requestHeaderValue\']",
                "[\'nullRequestHeaderValueKey\':null]",
                "[null:\'nullRequestHeaderKeyValue\']");
    }
}
