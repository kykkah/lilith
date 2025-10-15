/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.HttpRequestUrlCondition;
import de.huxhorn.sulky.conditions.Condition;

import java.util.List;
import java.util.Set;

class FocusHttpRequestUrlActionTest extends AbstractFilterActionTestBase {

    @Override
    protected FilterAction createAction() {
        return new FocusHttpRequestUrlAction();
    }

    @Override
    protected Set<Integer> expectedEnabledIndices() {
        return Set.of(74, 75);
    }

    @Override
    protected List<String> expectedSearchStrings() {
        return List.of(
                "GET /?foo=bar&foo=schnurz HTTP/1.1",
                "GET /index.html?foo=bar&foo=schnurz HTTP/1.1"
        );
    }

    @Override
    protected Class<? extends Condition> expectedConditionClass() {
        return HttpRequestUrlCondition.class;
    }
}
