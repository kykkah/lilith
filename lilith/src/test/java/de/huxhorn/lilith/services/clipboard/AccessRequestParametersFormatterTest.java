package de.huxhorn.lilith.services.clipboard;

import java.util.List;
import java.util.Set;

class AccessRequestParametersFormatterTest extends AbstractClipboardFormatterTest {

    @Override
    protected AccessRequestParametersFormatter createInstance() {
        return new AccessRequestParametersFormatter();
    }

    @Override
    protected Set<Integer> expectedIndices() {
        return Set.of(109, 110, 111, 112, 113);
    }

    @Override
    protected List<String> expectedResults() {
        return List.of(
                "[\'nullRequestParameterValueKey\':null]",
                "[\'requestParameterKey\':[]]",
                "[\'requestParameterKey\':[\'requestParameterValue1\', \'requestParameterValue2\']]",
                "[\'requestParameterKey\':[\'requestParameterValue1\', null, \'requestParameterValue3\']]",
                "[null:[\'nullRequestHeaderKeyValue\']]");
    }
}
