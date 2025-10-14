package de.huxhorn.lilith.services.clipboard;

import java.util.List;
import java.util.Set;

class AccessRequestUrlFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected AccessRequestUrlFormatter createInstance() {
        return new AccessRequestUrlFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(74, 75);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "GET /?foo=bar&foo=schnurz HTTP/1.1",
                "GET /index.html?foo=bar&foo=schnurz HTTP/1.1");
    }
}
