package de.huxhorn.lilith.services.clipboard;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Message;
import de.huxhorn.lilith.data.logging.ThreadInfo;
import de.huxhorn.lilith.data.logging.ThrowableInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FormatterToolsTest {

    private static final Map<FormatterValue, String> FOO_MAP = new HashMap<>();
    private static final Map<String, String> NULL_MAP = new HashMap<>();

    static {
        FOO_MAP.put(new FormatterValue("1"), "x");
        NULL_MAP.put(null, "x");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("isNullOrEmptyArguments")
    void isNullOrEmpty(Object input, boolean expectedResult) {
        assertEquals(expectedResult, FormatterTools.isNullOrEmpty(input));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("toStringOrNullArguments")
    void toStringOrNull(Object input, String expectedResult) {
        assertEquals(expectedResult, FormatterTools.toStringOrNull(input));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveAccessEventArguments")
    void resolveAccessEvent(Object input, boolean expectedPresent) {
        Optional<AccessEvent> optional = FormatterTools.resolveAccessEvent(input);
        assertEquals(expectedPresent, optional.isPresent());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveLoggingEventArguments")
    void resolveLoggingEvent(Object input, boolean expectedPresent) {
        Optional<LoggingEvent> optional = FormatterTools.resolveLoggingEvent(input);
        assertEquals(expectedPresent, optional.isPresent());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveFormattedMessageArguments")
    void resolveFormattedMessage(Object input, boolean expectedPresent, String expectedValue) {
        Optional<String> optional = FormatterTools.resolveFormattedMessage(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveMessagePatternArguments")
    void resolveMessagePattern(Object input, boolean expectedPresent, String expectedValue) {
        Optional<String> optional = FormatterTools.resolveMessagePattern(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveCallStackArguments")
    void resolveCallStack(
            Object input, boolean expectedPresent, ExtendedStackTraceElement[] expectedValue) {
        Optional<ExtendedStackTraceElement[]> optional = FormatterTools.resolveCallStack(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertArrayEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveThrowableInfoArguments")
    void resolveThrowableInfo(Object input, boolean expectedPresent, ThrowableInfo expectedValue) {
        Optional<ThrowableInfo> optional = FormatterTools.resolveThrowableInfo(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveThrowableInfoNameArguments")
    void resolveThrowableInfoName(Object input, boolean expectedPresent, String expectedValue) {
        Optional<String> optional = FormatterTools.resolveThrowableInfoName(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveThreadNameArguments")
    void resolveThreadName(Object input, boolean expectedPresent, String expectedValue) {
        Optional<String> optional = FormatterTools.resolveThreadName(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("resolveThreadGroupNameArguments")
    void resolveThreadGroupName(Object input, boolean expectedPresent, String expectedValue) {
        Optional<String> optional = FormatterTools.resolveThreadGroupName(input);
        assertEquals(expectedPresent, optional.isPresent());
        if (expectedPresent) {
            assertEquals(expectedValue, optional.orElseThrow());
        } else {
            assertEquals(expectedValue, optional.orElse(null));
        }
    }

    static Stream<Arguments> isNullOrEmptyArguments() {
        return Stream.of(
                arg("null", null, true),
                arg("emptyString", "", true),
                arg("emptyMap", Collections.emptyMap(), true),
                arg("emptySet", Collections.emptySet(), true),
                arg("emptyList", Collections.emptyList(), true),
                arg("emptyObjectArray", new Object[0], true),
                arg("stringValue", "foo", false),
                arg("integerValue", 1, false),
                arg("mapWithValue", Map.of("fooKey", "fooValue"), false),
                arg("setWithValue", Set.of("foo"), false),
                arg("listWithValue", List.of("foo"), false),
                arg("objectArrayWithValue", new Object[] {"foo"}, false),
                arg("treeSetWithValue", new TreeSet<>(Set.of("foo")), false),
                arg("treeMapWithValue", new TreeMap<>(Map.of("fooKey", "fooValue")), false),
                arg("setWithNull", new HashSet<>(Collections.singletonList(null)), false),
                arg("listWithNull", Collections.singletonList(null), false),
                arg("objectArrayWithNull", new Object[] {null}, false),
                arg(
                        "setWithFooInstance",
                        new HashSet<>(Collections.singleton(new FormatterValue("1"))),
                        false),
                arg("fooMap", FOO_MAP, false),
                arg("nullMap", NULL_MAP, false));
    }

    static Stream<Arguments> toStringOrNullArguments() {
        return Stream.of(
                arg("null", null, null),
                arg("emptyString", "", null),
                arg("emptyMap", Collections.emptyMap(), null),
                arg("emptySet", Collections.emptySet(), null),
                arg("emptyList", Collections.emptyList(), null),
                arg("emptyObjectArray", new Object[0], null),
                arg("stringValue", "foo", "foo"),
                arg("integerValue", 1, "1"),
                arg(
                        "mapWithValue",
                        new HashMap<>(Collections.singletonMap("fooKey", "fooValue")),
                        "[\'fooKey\':\'fooValue\']"),
                arg(
                        "setWithValue",
                        new HashSet<>(Collections.singleton("foo")),
                        "[\'foo\']"),
                arg("listWithValue", List.of("foo"), "[\'foo\']"),
                arg("objectArrayWithValue", new Object[] {"foo"}, "[\'foo\']"),
                arg("treeSetWithValue", new TreeSet<>(Set.of("foo")), "[\'foo\']"),
                arg("treeMapWithValue", new TreeMap<>(Map.of("fooKey", "fooValue")), "[\'fooKey\':\'fooValue\']"),
                arg("setWithNull", new HashSet<>(Collections.singletonList(null)), "[null]"),
                arg("listWithNull", Collections.singletonList(null), "[null]"),
                arg("objectArrayWithNull", new Object[] {null}, "[null]"),
                arg(
                        "setWithFooInstance",
                        new HashSet<>(Collections.singleton(new FormatterValue("1"))),
                        "[FormatterValue{value=\'1\'}]"),
                arg("fooMap", FOO_MAP, "[FormatterValue{value=\'1\'}:\'x\']"),
                arg("nullMap", NULL_MAP, "[null:\'x\']"));
    }

    static Stream<Arguments> resolveAccessEventArguments() {
        return Stream.of(
                arg("null", null, false),
                arg("integer", 1, false),
                arg("accessEvent", accessWrapper(new AccessEvent()), true),
                arg("loggingEvent", loggingWrapper(new LoggingEvent()), false));
    }

    static Stream<Arguments> resolveLoggingEventArguments() {
        return Stream.of(
                arg("null", null, false),
                arg("integer", 1, false),
                arg("accessEvent", accessWrapper(new AccessEvent()), false),
                arg("loggingEvent", loggingWrapper(new LoggingEvent()), true));
    }

    static Stream<Arguments> resolveFormattedMessageArguments() {
        LoggingEvent loggingWithoutMessage = new LoggingEvent();

        LoggingEvent emptyMessageEvent = new LoggingEvent();
        emptyMessageEvent.setMessage(new Message(""));

        LoggingEvent messageEvent = new LoggingEvent();
        messageEvent.setMessage(new Message("foo"));

        LoggingEvent formattedMessageEvent = new LoggingEvent();
        formattedMessageEvent.setMessage(new Message("{}", new String[] {"foo"}));

        return Stream.of(
                arg("null", null, false, (String) null),
                arg("integer", 1, false, (String) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (String) null),
                arg("loggingWithoutMessage", loggingWrapper(loggingWithoutMessage), false, (String) null),
                arg("loggingEmptyMessage", loggingWrapper(emptyMessageEvent), false, (String) null),
                arg("loggingWithMessage", loggingWrapper(messageEvent), true, "foo"),
                arg("loggingWithFormattedMessage", loggingWrapper(formattedMessageEvent), true, "foo"));
    }

    static Stream<Arguments> resolveMessagePatternArguments() {
        LoggingEvent loggingWithoutMessage = new LoggingEvent();

        LoggingEvent emptyMessageEvent = new LoggingEvent();
        emptyMessageEvent.setMessage(new Message(""));

        LoggingEvent messageEvent = new LoggingEvent();
        messageEvent.setMessage(new Message("foo"));

        LoggingEvent patternWithArgumentsEvent = new LoggingEvent();
        patternWithArgumentsEvent.setMessage(new Message("{}", new String[] {"foo"}));

        return Stream.of(
                arg("null", null, false, (String) null),
                arg("integer", 1, false, (String) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (String) null),
                arg("loggingWithoutMessage", loggingWrapper(loggingWithoutMessage), false, (String) null),
                arg("loggingEmptyMessage", loggingWrapper(emptyMessageEvent), false, (String) null),
                arg("loggingWithMessage", loggingWrapper(messageEvent), true, "foo"),
                arg("loggingWithPatternAndArgs", loggingWrapper(patternWithArgumentsEvent), true, "{}"));
    }

    static Stream<Arguments> resolveCallStackArguments() {
        LoggingEvent loggingWithoutCallStack = new LoggingEvent();

        LoggingEvent emptyCallStackEvent = new LoggingEvent();
        emptyCallStackEvent.setCallStack(new ExtendedStackTraceElement[0]);

        ExtendedStackTraceElement[] callStack = createCallStack();
        LoggingEvent callStackEvent = new LoggingEvent();
        callStackEvent.setCallStack(callStack);

        return Stream.of(
                arg("null", null, false, (ExtendedStackTraceElement[]) null),
                arg("integer", 1, false, (ExtendedStackTraceElement[]) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (ExtendedStackTraceElement[]) null),
                arg(
                        "loggingWithoutCallStack",
                        loggingWrapper(loggingWithoutCallStack),
                        false,
                        (ExtendedStackTraceElement[]) null),
                arg(
                        "loggingEmptyCallStack",
                        loggingWrapper(emptyCallStackEvent),
                        false,
                        (ExtendedStackTraceElement[]) null),
                arg("callStack", loggingWrapper(callStackEvent), true, callStack));
    }

    static Stream<Arguments> resolveThrowableInfoArguments() {
        LoggingEvent loggingWithoutThrowable = new LoggingEvent();

        ThrowableInfo simpleThrowable = createThrowableInfo("java.lang.RuntimeException");
        LoggingEvent simpleThrowableEvent = new LoggingEvent();
        simpleThrowableEvent.setThrowable(simpleThrowable);

        ThrowableInfo emptyNameThrowable = createThrowableInfo(null);
        LoggingEvent emptyNameEvent = new LoggingEvent();
        emptyNameEvent.setThrowable(emptyNameThrowable);

        ThrowableInfo blankNameThrowable = createThrowableInfo("");
        LoggingEvent blankNameEvent = new LoggingEvent();
        blankNameEvent.setThrowable(blankNameThrowable);

        return Stream.of(
                arg("null", null, false, (ThrowableInfo) null),
                arg("integer", 1, false, (ThrowableInfo) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (ThrowableInfo) null),
                arg(
                        "loggingWithoutThrowable",
                        loggingWrapper(loggingWithoutThrowable),
                        false,
                        (ThrowableInfo) null),
                arg("throwableWithNullName", loggingWrapper(emptyNameEvent), true, emptyNameThrowable),
                arg("throwableWithEmptyName", loggingWrapper(blankNameEvent), true, blankNameThrowable),
                arg("throwableWithName", loggingWrapper(simpleThrowableEvent), true, simpleThrowable));
    }

    static Stream<Arguments> resolveThrowableInfoNameArguments() {
        LoggingEvent loggingWithoutThrowable = new LoggingEvent();

        ThrowableInfo nullNameThrowable = createThrowableInfo(null);
        LoggingEvent nullNameEvent = new LoggingEvent();
        nullNameEvent.setThrowable(nullNameThrowable);

        ThrowableInfo emptyNameThrowable = createThrowableInfo("");
        LoggingEvent emptyNameEvent = new LoggingEvent();
        emptyNameEvent.setThrowable(emptyNameThrowable);

        ThrowableInfo nameThrowable = createThrowableInfo("foo");
        LoggingEvent nameEvent = new LoggingEvent();
        nameEvent.setThrowable(nameThrowable);

        return Stream.of(
                arg("null", null, false, (String) null),
                arg("integer", 1, false, (String) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (String) null),
                arg("loggingWithoutThrowable", loggingWrapper(loggingWithoutThrowable), false, (String) null),
                arg("throwableWithNullName", loggingWrapper(nullNameEvent), false, (String) null),
                arg("throwableWithEmptyName", loggingWrapper(emptyNameEvent), false, (String) null),
                arg("throwableWithName", loggingWrapper(nameEvent), true, "foo"));
    }

    static Stream<Arguments> resolveThreadNameArguments() {
        LoggingEvent loggingWithoutThread = new LoggingEvent();

        ThreadInfo nullThreadInfo = threadInfo(null, null);
        LoggingEvent nullThreadInfoEvent = new LoggingEvent();
        nullThreadInfoEvent.setThreadInfo(nullThreadInfo);

        ThreadInfo emptyThreadInfo = threadInfo("", "");
        LoggingEvent emptyThreadInfoEvent = new LoggingEvent();
        emptyThreadInfoEvent.setThreadInfo(emptyThreadInfo);

        ThreadInfo threadInfo = threadInfo("foo", "bar");
        LoggingEvent threadInfoEvent = new LoggingEvent();
        threadInfoEvent.setThreadInfo(threadInfo);

        return Stream.of(
                arg("null", null, false, (String) null),
                arg("integer", 1, false, (String) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (String) null),
                arg("loggingWithoutThread", loggingWrapper(loggingWithoutThread), false, (String) null),
                arg("nullThreadInfo", loggingWrapper(nullThreadInfoEvent), false, (String) null),
                arg("emptyThreadInfo", loggingWrapper(emptyThreadInfoEvent), false, (String) null),
                arg("threadInfo", loggingWrapper(threadInfoEvent), true, "foo"));
    }

    static Stream<Arguments> resolveThreadGroupNameArguments() {
        LoggingEvent loggingWithoutThread = new LoggingEvent();

        ThreadInfo nullThreadInfo = threadInfo(null, null);
        LoggingEvent nullThreadInfoEvent = new LoggingEvent();
        nullThreadInfoEvent.setThreadInfo(nullThreadInfo);

        ThreadInfo emptyThreadInfo = threadInfo("", "");
        LoggingEvent emptyThreadInfoEvent = new LoggingEvent();
        emptyThreadInfoEvent.setThreadInfo(emptyThreadInfo);

        ThreadInfo threadInfo = threadInfo("foo", "bar");
        LoggingEvent threadInfoEvent = new LoggingEvent();
        threadInfoEvent.setThreadInfo(threadInfo);

        return Stream.of(
                arg("null", null, false, (String) null),
                arg("integer", 1, false, (String) null),
                arg("accessEvent", accessWrapper(new AccessEvent()), false, (String) null),
                arg("loggingWithoutThread", loggingWrapper(loggingWithoutThread), false, (String) null),
                arg("nullThreadInfo", loggingWrapper(nullThreadInfoEvent), false, (String) null),
                arg("emptyThreadInfo", loggingWrapper(emptyThreadInfoEvent), false, (String) null),
                arg("threadInfo", loggingWrapper(threadInfoEvent), true, "bar"));
    }

    private static Arguments arg(String description, Object input, boolean expected) {
        return Arguments.of(Named.of(description, input), expected);
    }

    private static Arguments arg(String description, Object input, String expected) {
        return Arguments.of(Named.of(description, input), expected);
    }

    private static Arguments arg(
            String description, Object input, boolean expectedPresent, String expectedValue) {
        return Arguments.of(Named.of(description, input), expectedPresent, expectedValue);
    }

    private static Arguments arg(
            String description,
            Object input,
            boolean expectedPresent,
            ExtendedStackTraceElement[] expectedValue) {
        return Arguments.of(Named.of(description, input), expectedPresent, expectedValue);
    }

    private static Arguments arg(
            String description, Object input, boolean expectedPresent, ThrowableInfo expectedValue) {
        return Arguments.of(Named.of(description, input), expectedPresent, expectedValue);
    }

    private static EventWrapper<AccessEvent> accessWrapper(AccessEvent event) {
        return new EventWrapper<>(null, event);
    }

    private static EventWrapper<LoggingEvent> loggingWrapper(LoggingEvent event) {
        return new EventWrapper<>(null, event);
    }

    private static ExtendedStackTraceElement[] createCallStack() {
        return new ExtendedStackTraceElement[] {new ExtendedStackTraceElement()};
    }

    private static ThrowableInfo createThrowableInfo(String name) {
        ThrowableInfo throwableInfo = new ThrowableInfo();
        throwableInfo.setName(name);
        throwableInfo.setOmittedElements(0);
        return throwableInfo;
    }

    private static ThreadInfo threadInfo(String name, String groupName) {
        ThreadInfo info = new ThreadInfo();
        info.setName(name);
        info.setGroupName(groupName);
        return info;
    }

    private static final class FormatterValue {
        private final String value;

        private FormatterValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FormatterValue)) {
                return false;
            }
            FormatterValue other = (FormatterValue) obj;
            return value != null ? value.equals(other.value) : other.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "FormatterValue{value='" + value + "'}";
        }
    }
}
