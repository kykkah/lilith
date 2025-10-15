/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith.swing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

final class LilithActionGroups {

    private LilithActionGroups() {
    }

    static List<LilithActionId> mainMenuActions() {
        return List.of(
                LilithActionId.FILE,
                LilithActionId.EDIT,
                LilithActionId.SEARCH,
                LilithActionId.VIEW,
                LilithActionId.WINDOW,
               LilithActionId.HELP
        );
    }

    static List<LilithActionId> fileMenuActions() {
        return List.of(
                LilithActionId.OPEN,
                LilithActionId.RECENT_FILES,
                LilithActionId.OPEN_INACTIVE,
                LilithActionId.CLEAN_ALL_INACTIVE_LOGS,
                LilithActionId.IMPORT,
                LilithActionId.EXPORT,
                LilithActionId.PREFERENCES,
                LilithActionId.EXIT
        );
    }

    static List<LilithActionId> recentFilesMenuActions() {
        return List.of(LilithActionId.CLEAR_RECENT_FILES);
    }

    static List<LilithActionId> editMenuBaseActions() {
        return List.of(
                LilithActionId.COPY_SELECTION,
                LilithActionId.COPY_HTML,
                LilithActionId.CUSTOM_COPY,
                LilithActionId.GO_TO_SOURCE,
                LilithActionId.PASTE_STACK_TRACE_ELEMENT
        );
    }

    static List<LilithActionId> editMenuLoggingBaseActions() {
        return List.of(
                LilithActionId.COPY_JSON,
                LilithActionId.COPY_XML,
                LilithActionId.COPY_MESSAGE,
                LilithActionId.COPY_MESSAGE_PATTERN,
                LilithActionId.COPY_LOGGER_NAME,
                LilithActionId.COPY_THROWABLE,
                LilithActionId.COPY_THROWABLE_NAME,
                LilithActionId.COPY_CALL_LOCATION,
                LilithActionId.COPY_CALL_STACK,
                LilithActionId.COPY_THREAD_NAME,
                LilithActionId.COPY_THREAD_GROUP_NAME,
                LilithActionId.COPY_MARKER,
                LilithActionId.COPY_MDC,
                LilithActionId.COPY_NDC
        );
    }

    static List<LilithActionId> editMenuAccessBaseActions() {
        return List.of(
                LilithActionId.COPY_REQUEST_URI,
                LilithActionId.COPY_REQUEST_URL,
                LilithActionId.COPY_REQUEST_HEADERS,
                LilithActionId.COPY_REQUEST_PARAMETERS,
                LilithActionId.COPY_RESPONSE_HEADERS
        );
    }

    static List<LilithActionId> editMenuActions() {
        List<LilithActionId> result = new ArrayList<>(editMenuBaseActions());
        result.addAll(editMenuLoggingBaseActions());
        result.addAll(editMenuAccessBaseActions());
        return result;
    }

    static List<LilithActionId> editMenuLoggingActions() {
        List<LilithActionId> result = new ArrayList<>(editMenuBaseActions());
        result.addAll(editMenuLoggingBaseActions());
        return result;
    }

    static List<LilithActionId> editMenuAccessActions() {
        List<LilithActionId> result = new ArrayList<>(editMenuBaseActions());
        result.addAll(editMenuAccessBaseActions());
        return result;
    }

    static List<LilithActionId> searchMenuActions() {
        return List.of(
                LilithActionId.FIND,
                LilithActionId.RESET_FIND,
                LilithActionId.FIND_PREVIOUS,
                LilithActionId.FIND_NEXT,
                LilithActionId.FIND_PREVIOUS_ACTIVE,
                LilithActionId.FIND_NEXT_ACTIVE,
                LilithActionId.SAVE_CONDITION,
                LilithActionId.FOCUS,
                LilithActionId.EXCLUDE,
                LilithActionId.SHOW_UNFILTERED_EVENT
        );
    }

    static List<LilithActionId> viewMenuActions() {
        return List.of(
                LilithActionId.TAIL,
                LilithActionId.CLEAR,
                LilithActionId.ATTACH,
                LilithActionId.DISCONNECT,
                LilithActionId.FOCUS_EVENTS,
                LilithActionId.FOCUS_MESSAGE,
                LilithActionId.EDIT_SOURCE_NAME,
                LilithActionId.ZOOM_IN,
                LilithActionId.ZOOM_OUT,
                LilithActionId.RESET_ZOOM,
                LilithActionId.LAYOUT,
                LilithActionId.NEXT_VIEW,
                LilithActionId.PREVIOUS_VIEW,
                LilithActionId.CLOSE_FILTER,
                LilithActionId.CLOSE_OTHER_FILTERS,
                LilithActionId.CLOSE_ALL_FILTERS
        );
    }

    static List<LilithActionId> layoutMenuActions() {
        return List.of(
                LilithActionId.COLUMNS,
                LilithActionId.SAVE_LAYOUT,
                LilithActionId.RESET_LAYOUT
        );
    }

    static List<LilithActionId> windowMenuActions() {
        return List.of(
                LilithActionId.TASK_MANAGER,
                LilithActionId.CLOSE_ALL,
                LilithActionId.CLOSE_ALL_OTHER,
                LilithActionId.MINIMIZE_ALL,
                LilithActionId.MINIMIZE_OTHER,
                LilithActionId.REMOVE_INACTIVE,
                LilithActionId.VIEW_LILITH_LOGS,
                LilithActionId.VIEW_GLOBAL_CLASSIC_LOGS,
                LilithActionId.VIEW_GLOBAL_ACCESS_LOGS
        );
    }

    static List<LilithActionId> helpMenuActions() {
        return List.of(
                LilithActionId.HELP_TOPICS,
                LilithActionId.LOVE,
                LilithActionId.TIP_OF_THE_DAY,
                LilithActionId.CHECK_FOR_UPDATE,
                LilithActionId.TROUBLESHOOTING,
                LilithActionId.DEBUG,
                LilithActionId.ABOUT
        );
    }

    static List<LilithActionId> miscActions() {
        return List.of(LilithActionId.REPLACE_FILTER);
    }

    static List<List<LilithActionId>> combinedActionGroups() {
        List<List<LilithActionId>> result = new ArrayList<>();
        result.add(mainMenuActions());
        result.add(fileMenuActions());
        result.add(recentFilesMenuActions());
        result.add(editMenuLoggingActions());
        result.add(editMenuAccessActions());
        result.add(searchMenuActions());
        result.add(viewMenuActions());
        result.add(layoutMenuActions());
        result.add(windowMenuActions());
        result.add(helpMenuActions());
        result.add(miscActions());
        return result;
    }

    static Set<LilithActionId> combinedActions() {
        Set<LilithActionId> result = new LinkedHashSet<>();
        combinedActionGroups().forEach(result::addAll);
        return result;
    }
}
