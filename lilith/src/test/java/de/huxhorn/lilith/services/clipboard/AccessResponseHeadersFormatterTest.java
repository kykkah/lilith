package de.huxhorn.lilith.services.clipboard;

import java.util.List;
import java.util.Set;

class AccessResponseHeadersFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected AccessResponseHeadersFormatter createInstance() {
        return new AccessResponseHeadersFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(105, 106, 107);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "[\'responseHeaderKey\':\'responseHeaderValue\']",
                "[\'nullResponseHeaderValueKey\':null]",
                "[null:\'nullResponseHeaderKeyValue\']");
    }
}
