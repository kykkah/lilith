package de.huxhorn.lilith.services.clipboard;

import java.util.List;
import java.util.Set;

class AccessRequestUriFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected AccessRequestUriFormatter createInstance() {
        return new AccessRequestUriFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(72, 73, 122);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of("/", "/index.html", "/foo/bar/foobar");
    }
}
