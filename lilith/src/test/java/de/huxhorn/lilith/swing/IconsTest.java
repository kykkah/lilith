/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IconsTest {

    private static final Map<LilithFrameId, Integer> FRAME_ID_IMAGE_COUNT = new EnumMap<>(LilithFrameId.class);
    private static final List<LoggingViewState> LOGGING_VIEW_STATES = new ArrayList<>();

    static {
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.HELP, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.MAIN, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_GLOBAL, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_GLOBAL_DISABLED, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_ACTIVE, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_ACTIVE_DISABLED, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_INACTIVE, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_INACTIVE_DISABLED, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_UPDATING_FILE, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_UPDATING_FILE_DISABLED, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_STALE_FILE, 1);
        FRAME_ID_IMAGE_COUNT.put(LilithFrameId.VIEW_STATE_STALE_FILE_DISABLED, 1);

        LOGGING_VIEW_STATES.addAll(List.of(LoggingViewState.values()));
        LOGGING_VIEW_STATES.add(null);
    }

    @BeforeAll
    static void setHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @ParameterizedTest(name = "{0} has toolbar icon")
    @MethodSource("toolbarActionIds")
    void toolbarActionsHaveIcons(LilithActionId actionId) {
        assertNotNull(Icons.resolveToolbarIcon(actionId));
    }

    @ParameterizedTest(name = "{0} has menu icon")
    @MethodSource("allActionIds")
    void actionsHaveMenuIcons(LilithActionId actionId) {
        assertNotNull(Icons.resolveMenuIcon(actionId));
    }

    @Test
    void frameIdMappingProducesUniqueValues() {
        Set<LilithFrameId> resolved = new java.util.HashSet<>();
        for (LoggingViewState viewState : LOGGING_VIEW_STATES) {
            resolved.add(Icons.frameIdForViewState(viewState, true));
            resolved.add(Icons.frameIdForViewState(viewState, false));
        }
        assertEquals(2 * (LoggingViewState.values().length + 1), resolved.size());
    }

    @Test
    void frameIdImageCountCoversAllFrameIds() {
        assertEquals(LilithFrameId.values().length, FRAME_ID_IMAGE_COUNT.size());
    }

    @ParameterizedTest(name = "resolveFrameIcon({0}) returns icon")
    @MethodSource("frameIds")
    void resolveFrameIconReturnsIcon(LilithFrameId frameId) {
        assertNotNull(Icons.resolveFrameIcon(frameId));
    }

    @ParameterizedTest(name = "resolveFrameIconImages({0}) returns expected size {1}")
    @MethodSource("frameIdImageData")
    void resolveFrameIconImagesMatchesExpectedSize(LilithFrameId frameId, Integer expectedSize) {
        List<?> images = Icons.resolveFrameIconImages(frameId);
        assertNotNull(images);
        assertEquals(expectedSize.intValue(), images.size());
    }

    @Test
    void resolveEmptyMenuIconReturnsIcon() {
        assertNotNull(Icons.resolveEmptyMenuIcon());
    }

    @ParameterizedTest(name = "resolveFrameIcon({0}, false) returns icon")
    @MethodSource("loggingViewStates")
    void resolveFrameIconForViewStateFalse(LoggingViewState viewState) {
        assertNotNull(Icons.resolveFrameIcon(viewState, false));
    }

    @ParameterizedTest(name = "resolveFrameIcon({0}, true) returns icon")
    @MethodSource("loggingViewStates")
    void resolveFrameIconForViewStateTrue(LoggingViewState viewState) {
        assertNotNull(Icons.resolveFrameIcon(viewState, true));
    }

    @ParameterizedTest(name = "resolveFrameIconImages({0}, false) returns images")
    @MethodSource("loggingViewStates")
    void resolveFrameIconImagesFalse(LoggingViewState viewState) {
        List<?> images = Icons.resolveFrameIconImages(viewState, false);
        assertNotNull(images);
        assertFalse(images.isEmpty());
    }

    @ParameterizedTest(name = "resolveFrameIconImages({0}, true) returns images")
    @MethodSource("loggingViewStates")
    void resolveFrameIconImagesTrue(LoggingViewState viewState) {
        List<?> images = Icons.resolveFrameIconImages(viewState, true);
        assertNotNull(images);
        assertFalse(images.isEmpty());
    }

    @Test
    void resolveImageIconNullThrows() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> Icons.resolveImageIcon(null));
        assertEquals("resourcePath must not be null!", exception.getMessage());
    }

    @Test
    void resolveImageIconInvalidResourceThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Icons.resolveImageIcon("invalidResource"));
        assertEquals("Failed to create ImageIcon from resource 'invalidResource'!", exception.getMessage());
    }

    private static Stream<LilithActionId> toolbarActionIds() {
        return Stream.of(
                LilithActionId.TAIL,
                LilithActionId.CLEAR,
                LilithActionId.DISCONNECT,
                LilithActionId.FIND,
                LilithActionId.FIND_PREVIOUS,
                LilithActionId.FIND_NEXT,
                LilithActionId.PREFERENCES,
                LilithActionId.LOVE
        );
    }

    private static Stream<LilithActionId> allActionIds() {
        return Stream.of(LilithActionId.values());
    }

    private static Stream<LilithFrameId> frameIds() {
        return Stream.of(LilithFrameId.values());
    }

    private static Stream<Arguments> frameIdImageData() {
        return FRAME_ID_IMAGE_COUNT.entrySet().stream()
                .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    private static Stream<LoggingViewState> loggingViewStates() {
        return LOGGING_VIEW_STATES.stream();
    }

}
