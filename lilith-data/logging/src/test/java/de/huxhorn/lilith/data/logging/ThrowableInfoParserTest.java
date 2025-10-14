/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2014 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Copyright 2007-2014 Joern Huxhorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.huxhorn.lilith.data.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThrowableInfoParserTest {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@ParameterizedTest
	@MethodSource("parseExamples")
	void parseExamples(String input, Consumer<ThrowableInfo> verifier) {
		ThrowableInfo parsed = ThrowableInfoParser.parse(input);
		assertNotNull(parsed);
		verifier.accept(parsed);
	}

	@ParameterizedTest
	@MethodSource("splitLinesExamples")
	void splitLinesExamples(String input, List<String> expected) {
		assertEquals(expected, ThrowableInfoParser.splitLines(input));
	}

	@ParameterizedTest
	@MethodSource("omittedMatcherExamples")
	void omittedMatcherExamples(String input, boolean matches, String indent, String amount) {
		Matcher matcher = ThrowableInfoParser.omittedMatcher(input);
		assertEquals(matches, matcher.matches());
		if(matches) {
			assertEquals(2, matcher.groupCount());
			assertEquals(input, matcher.group(0));
			assertEquals(indent, matcher.group(1));
			assertEquals(amount, matcher.group(2));
		}
	}

	@ParameterizedTest
	@MethodSource("atMatcherExamples")
	void atMatcherExamples(String input, boolean matches, String indent, String remainder) {
		Matcher matcher = ThrowableInfoParser.atMatcher(input);
		assertEquals(matches, matcher.matches());
		if(matches) {
			assertEquals(2, matcher.groupCount());
			assertEquals(input, matcher.group(0));
			assertEquals(indent, matcher.group(1));
			assertEquals(remainder, matcher.group(2));
		}
	}

	@ParameterizedTest
	@MethodSource("messageMatcherExamples")
	void messageMatcherExamples(String input, boolean matches, String indent, String prefix, String remainder) {
		Matcher matcher = ThrowableInfoParser.messageMatcher(input);
		assertEquals(matches, matcher.matches());
		if(matches) {
			assertEquals(3, matcher.groupCount());
			assertEquals(input, matcher.group(0));
			assertEquals(indent, matcher.group(1));
			assertEquals(prefix, matcher.group(2));
			assertEquals(remainder, matcher.group(3));
		}
	}

	private static Stream<Arguments> parseExamples() {
		return Stream.of(
			Arguments.of(simpleInput(), (Consumer<ThrowableInfo>) parsed -> {
				assertEquals("java.lang.RuntimeException", parsed.getName());
				assertNull(parsed.getMessage());
				assertStackTrace(parsed.getStackTrace(), new StackEntry("sun.reflect.NativeConstructorAccessorImpl", "newInstance0", null, ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER),
					new StackEntry("sun.reflect.NativeConstructorAccessorImpl", "newInstance", "NativeConstructorAccessorImpl.java", 57),
					new StackEntry("sun.reflect.DelegatingConstructorAccessorImpl", "newInstance", "DelegatingConstructorAccessorImpl.java", 45));
				assertNull(parsed.getSuppressed());
				assertNull(parsed.getCause());
			}),
			Arguments.of(outerInput(), (Consumer<ThrowableInfo>) parsed -> {
				assertEquals("java.lang.RuntimeException", parsed.getName());
				assertEquals("Outer", parsed.getMessage());
				assertStackTrace(parsed.getStackTrace(), new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox", "main", "Log4jSandbox.java", 78));
				ThrowableInfo[] suppressed = parsed.getSuppressed();
				assertNotNull(suppressed);
				assertEquals(2, suppressed.length);
				assertThrowable(suppressed[0], "java.lang.RuntimeException", "Suppressed1",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox", "main", "Log4jSandbox.java", 80));
				assertThrowable(suppressed[1], "java.lang.RuntimeException", "Suppressed2",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox", "main", "Log4jSandbox.java", 81));
				ThrowableInfo cause = parsed.getCause();
				assertNotNull(cause);
				assertThrowable(cause, "java.lang.RuntimeException", "Cause");
				assertEquals(1, cause.getOmittedElements());
			}),
			Arguments.of(complexInput(), (Consumer<ThrowableInfo>) parsed -> {
				assertEquals("java.lang.RuntimeException", parsed.getName());
				assertEquals("Hi.", parsed.getMessage());
				assertStackTrace(parsed.getStackTrace(),
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "execute", "Log4jSandbox.java", 49),
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox", "main", "Log4jSandbox.java", 86));
				ThrowableInfo cause = parsed.getCause();
				assertNotNull(cause);
				assertEquals("java.lang.RuntimeException", cause.getName());
				assertEquals("Hi Cause.", cause.getMessage());
				assertStackTrace(cause.getStackTrace(),
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 60),
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "execute", "Log4jSandbox.java", 45));
				assertEquals(1, cause.getOmittedElements());
				ThrowableInfo[] innerSuppressed = cause.getSuppressed();
				assertNotNull(innerSuppressed);
				assertEquals(4, innerSuppressed.length);
				assertThrowable(innerSuppressed[0], "java.lang.RuntimeException", null,
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 61));
				assertEquals(2, innerSuppressed[0].getOmittedElements());
				assertThrowable(innerSuppressed[1], "java.lang.RuntimeException", "Single line",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 62));
				assertEquals(2, innerSuppressed[1].getOmittedElements());
				ThrowableInfo thirdSuppressed = innerSuppressed[2];
				assertThrowable(thirdSuppressed, "java.lang.RuntimeException", "With cause and suppressed",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 63));
				ThrowableInfo[] nestedSuppressed = thirdSuppressed.getSuppressed();
				assertNotNull(nestedSuppressed);
				assertEquals(2, nestedSuppressed.length);
				assertThrowable(nestedSuppressed[0], "java.lang.RuntimeException", "Inner Suppressed",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 64));
				assertEquals(2, nestedSuppressed[0].getOmittedElements());
				ThrowableInfo nestedSecond = nestedSuppressed[1];
				assertThrowable(nestedSecond, "java.lang.RuntimeException", "Inner Suppressed with Cause",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 65));
				assertEquals(2, nestedSecond.getOmittedElements());
				ThrowableInfo nestedCause = nestedSecond.getCause();
				assertNotNull(nestedCause);
				assertEquals("java.lang.RuntimeException", nestedCause.getName());
				assertEquals("Inner Cause", nestedCause.getMessage());
				assertEquals(3, nestedCause.getOmittedElements());
				assertEquals(2, thirdSuppressed.getOmittedElements());
				ThrowableInfo thirdCause = thirdSuppressed.getCause();
				assertNotNull(thirdCause);
				assertEquals("java.lang.RuntimeException", thirdCause.getName());
				assertEquals("Cause", thirdCause.getMessage());
				assertEquals(3, thirdCause.getOmittedElements());
				ThrowableInfo fourthSuppressed = innerSuppressed[3];
				assertThrowable(fourthSuppressed, "java.lang.RuntimeException", "Multi\nline",
					new StackEntry("de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass", "foobar", "Log4jSandbox.java", 67));
				assertEquals(2, fourthSuppressed.getOmittedElements());
				assertNull(parsed.getSuppressed());
			}),
			Arguments.of(brokenInput(), (Consumer<ThrowableInfo>) parsed -> {
				assertNull(parsed.getName());
				assertNull(parsed.getMessage());
				assertStackTrace(parsed.getStackTrace(),
					new StackEntry("sun.reflect.NativeConstructorAccessorImpl", "newInstance0", null, ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER),
					new StackEntry("sun.reflect.NativeConstructorAccessorImpl", "newInstance", "NativeConstructorAccessorImpl.java", 57),
					new StackEntry("sun.reflect.DelegatingConstructorAccessorImpl", "newInstance", "DelegatingConstructorAccessorImpl.java", 45));
			})
		);
	}

	private static Stream<Arguments> splitLinesExamples() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of("", List.of("")),
			Arguments.of("Foo", List.of("Foo")),
			Arguments.of("Foo\r\n\r\n\nBar\n", Arrays.asList("Foo", "", "", "Bar")),
			Arguments.of("Foo\r\n\r\r\r\r\n\nBar\n", Arrays.asList("Foo", "", "", "Bar")),
			Arguments.of("Foo\n\n\nBar\n", Arrays.asList("Foo", "", "", "Bar")),
			Arguments.of("Foo\n\n\nBar\n\n", Arrays.asList("Foo", "", "", "Bar", "")),
			Arguments.of("\nFoo\n\n\nBar\n\n", Arrays.asList("", "Foo", "", "", "Bar", "")),
			Arguments.of("\nFoo\n\tx\n y\nBar\n\n", Arrays.asList("", "Foo", "\tx", " y", "Bar", ""))
		);
	}

	private static Stream<Arguments> omittedMatcherExamples() {
		return Stream.of(
			Arguments.of("\t... 17 more", true, "\t", "17"),
			Arguments.of("\t.. 17 more", false, null, null),
			Arguments.of("\t\t... 17 more", true, "\t\t", "17"),
			Arguments.of("... 17 more", false, null, null)
		);
	}

	private static Stream<Arguments> atMatcherExamples() {
		return Stream.of(
			Arguments.of("\tat foo bar", true, "\t", "foo bar"),
			Arguments.of("\tta foo bar", false, null, null),
			Arguments.of("\t\tat foo bar", true, "\t\t", "foo bar"),
			Arguments.of("\t\tat ", false, null, null),
			Arguments.of("at foo bar", false, null, null)
		);
	}

	private static Stream<Arguments> messageMatcherExamples() {
		return Stream.of(
			Arguments.of("\tCaused by: foo", true, "\t", ThrowableInfo.CAUSED_BY_PREFIX, "foo"),
			Arguments.of("foo", true, "", null, "foo"),
			Arguments.of("", true, "", null, ""),
			Arguments.of("\t\tSuppressed: bar", true, "\t\t", ThrowableInfo.SUPPRESSED_PREFIX, "bar")
		);
	}

	private static void assertThrowable(ThrowableInfo info, String name, String message, StackEntry... entries) {
		assertNotNull(info);
		assertEquals(name, info.getName());
		assertEquals(message, info.getMessage());
		assertStackTrace(info.getStackTrace(), entries);
	}

	private static void assertStackTrace(ExtendedStackTraceElement[] stack, StackEntry... entries) {
		if(entries == null) {
			assertNull(stack);
			return;
		}
		if(entries.length == 0) {
			if(stack != null) {
				assertEquals(0, stack.length);
			}
			return;
		}
		assertNotNull(stack);
		assertEquals(entries.length, stack.length);
		for(int i = 0; i < entries.length; i++) {
			ExtendedStackTraceElement element = stack[i];
			StackEntry entry = entries[i];
			assertEquals(entry.className(), element.getClassName());
			assertEquals(entry.methodName(), element.getMethodName());
			assertEquals(entry.fileName(), element.getFileName());
			assertEquals(entry.lineNumber(), element.getLineNumber());
		}
	}

	private static String simpleInput() {
		return "java.lang.RuntimeException" + LINE_SEPARATOR +
			"\tat sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)" + LINE_SEPARATOR +
			"\tat sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)" + LINE_SEPARATOR +
			"\tat sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)" + LINE_SEPARATOR;
	}

	private static String outerInput() {
		return "java.lang.RuntimeException: Outer" + LINE_SEPARATOR +
			"\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:78)" + LINE_SEPARATOR +
			"\tSuppressed: java.lang.RuntimeException: Suppressed1" + LINE_SEPARATOR +
			"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:80)" + LINE_SEPARATOR +
			"\tSuppressed: java.lang.RuntimeException: Suppressed2" + LINE_SEPARATOR +
			"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:81)" + LINE_SEPARATOR +
			"Caused by: java.lang.RuntimeException: Cause" + LINE_SEPARATOR +
			"\t... 1 more" + LINE_SEPARATOR;
	}

	private static String complexInput() {
		return "java.lang.RuntimeException: Hi." + LINE_SEPARATOR +
			"\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.execute(Log4jSandbox.java:49)" + LINE_SEPARATOR +
			"\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:86)" + LINE_SEPARATOR +
			"Caused by: java.lang.RuntimeException: Hi Cause." + LINE_SEPARATOR +
			"\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:60)" + LINE_SEPARATOR +
			"\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.execute(Log4jSandbox.java:45)" + LINE_SEPARATOR +
			"\t... 1 more" + LINE_SEPARATOR +
			"\tSuppressed: java.lang.RuntimeException" + LINE_SEPARATOR +
			"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:61)" + LINE_SEPARATOR +
			"\t\t... 2 more" + LINE_SEPARATOR +
			"\tSuppressed: java.lang.RuntimeException: Single line" + LINE_SEPARATOR +
			"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:62)" + LINE_SEPARATOR +
			"\t\t... 2 more" + LINE_SEPARATOR +
			"\tSuppressed: java.lang.RuntimeException: With cause and suppressed" + LINE_SEPARATOR +
			"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:63)" + LINE_SEPARATOR +
			"\t\t... 2 more" + LINE_SEPARATOR +
			"\t\tSuppressed: java.lang.RuntimeException: Inner Suppressed" + LINE_SEPARATOR +
			"\t\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:64)" + LINE_SEPARATOR +
			"\t\t\t... 2 more" + LINE_SEPARATOR +
			"\t\tSuppressed: java.lang.RuntimeException: Inner Suppressed with Cause" + LINE_SEPARATOR +
			"\t\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:65)" + LINE_SEPARATOR +
			"\t\t\t... 2 more" + LINE_SEPARATOR +
			"\t\tCaused by: java.lang.RuntimeException: Inner Cause" + LINE_SEPARATOR +
			"\t\t\t... 3 more" + LINE_SEPARATOR +
			"\tCaused by: java.lang.RuntimeException: Cause" + LINE_SEPARATOR +
			"\t\t... 3 more" + LINE_SEPARATOR +
			"\tSuppressed: java.lang.RuntimeException: Multi" + LINE_SEPARATOR +
			"line" + LINE_SEPARATOR +
			"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox$InnerClass.foobar(Log4jSandbox.java:67)" + LINE_SEPARATOR +
			"\t\t... 2 more" + LINE_SEPARATOR;
	}

	private static String brokenInput() {
		return "\tat sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)" + LINE_SEPARATOR +
			"\tat sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)" + LINE_SEPARATOR +
			"\tat sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)" + LINE_SEPARATOR;
	}

	private record StackEntry(String className, String methodName, String fileName, int lineNumber) {}
}
