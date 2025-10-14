/*
 * Lilith - a log event viewer.
Copyright (C) 2007-2016 Joern Huxhorn
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
 * Copyright 2007-2016 Joern Huxhorn
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

import static de.huxhorn.sulky.junit.JUnitTools.testClone;
import static de.huxhorn.sulky.junit.JUnitTools.testSerialization;
import static de.huxhorn.sulky.junit.JUnitTools.testXmlSerialization;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThrowableInfoTest {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@ParameterizedTest
	@MethodSource("toStringExamples")
	void toStringMatchesOriginal(ThrowableInfo throwableInfo, String expectedString) {
		assertEquals(expectedString, throwableInfo.toString());
	}

	@ParameterizedTest
	@MethodSource("inputValues")
	void serializationRoundTrips(ThrowableInfo inputValue) throws Exception {
		ThrowableInfo other = testSerialization(inputValue);
		assertThrowableCopy(inputValue, other);
		assertNotSame(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("inputValues")
	void xmlSerializationRoundTrips(ThrowableInfo inputValue) throws Exception {
		ThrowableInfo other = testXmlSerialization(inputValue);
		assertThrowableCopy(inputValue, other);
		assertNotSame(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("inputValues")
	void cloningRoundTrips(ThrowableInfo inputValue) throws Exception {
		ThrowableInfo other = testClone(inputValue);
		assertThrowableCopy(inputValue, other);
		assertNotSame(inputValue, other);
	}

	@Test
	void serializationDefaultConstructor() throws Exception {
		ThrowableInfo inputValue = new ThrowableInfo();
		ThrowableInfo other = testSerialization(inputValue);
		assertThrowableCopy(inputValue, other);
	}

	@Test
	void xmlSerializationDefaultConstructor() throws Exception {
		ThrowableInfo inputValue = new ThrowableInfo();
		ThrowableInfo other = testXmlSerialization(inputValue);
		assertThrowableCopy(inputValue, other);
	}

	@Test
	void cloningDefaultConstructor() throws Exception {
		ThrowableInfo inputValue = new ThrowableInfo();
		ThrowableInfo other = testClone(inputValue);
		assertThrowableCopy(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("brokenInstances")
	void serializationBrokenInstances(ThrowableInfo inputValue) throws Exception {
		ThrowableInfo other = testSerialization(inputValue);
		assertThrowableCopy(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("brokenInstances")
	void xmlSerializationBrokenInstances(ThrowableInfo inputValue) throws Exception {
		ThrowableInfo other = testXmlSerialization(inputValue);
		assertThrowableCopy(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("brokenInstances")
	void cloningBrokenInstances(ThrowableInfo inputValue) throws Exception {
		ThrowableInfo other = testClone(inputValue);
		assertThrowableCopy(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("brokenInstances")
	void toStringBrokenInstances(ThrowableInfo inputValue) {
		assertNotNull(inputValue.toString());
	}

	@ParameterizedTest
	@MethodSource("inequalityPairs")
	void inequalityChecks(ThrowableInfo instance, ThrowableInfo other) {
		assertNotEqualsSymmetric(instance, other);
	}

	@Test
	void equalsBasicExpectations() {
		ThrowableInfo instance = new ThrowableInfo();
		ThrowableInfo other = throwable(info -> info.setName("foo"));

		assertEquals(instance, instance);
		assertNotEqualsSymmetric(instance, null);
		assertNotEqualsSymmetric(instance, new ThrowableInfoTest());
		assertNotEqualsSymmetric(instance, other);
	}

	@Test
	void equalsWithInternalIdentityEquality() {
		ThrowableInfo cause = new ThrowableInfo();
		ThrowableInfo instance = throwable(info -> info.setCause(cause));
		ThrowableInfo other = throwable(info -> info.setCause(cause));

		assertEquals(instance, instance);
		assertEquals(instance, other);
		assertEquals(other, instance);
	}

	@Test
	void toStringSpecialCases() {
		assertEquals("java.lang.RuntimeException" + LINE_SEPARATOR,
			throwable(info -> info.setName("java.lang.RuntimeException")).toString());
		assertEquals("java.lang.RuntimeException" + LINE_SEPARATOR,
			throwable(info -> {
				info.setName("java.lang.RuntimeException");
				info.setMessage("java.lang.RuntimeException");
			}).toString());
		assertEquals("java.lang.RuntimeException: foo" + LINE_SEPARATOR,
			throwable(info -> {
				info.setName("java.lang.RuntimeException");
				info.setMessage("foo");
			}).toString());
	}

	private static Stream<Arguments> toStringExamples() {
		List<String> parseInputValues = parseInputValues();
		return Stream.iterate(0, i -> i + 1)
			.limit(parseInputValues.size())
			.map(i -> {
				String input = parseInputValues.get(i);
				ThrowableInfo parsed = ThrowableInfoParser.parse(input);
				return Arguments.of(parsed, input);
			});
	}

	private static Stream<ThrowableInfo> inputValues() {
		return Stream.of(
			throwable(info -> info.setName("name")),
			throwable(info -> info.setMessage("message")),
			throwable(info -> info.setOmittedElements(3)),
			throwable(info -> info.setStackTrace(new ExtendedStackTraceElement[]{
				extendedElement(e -> {
					e.setClassName("className");
					e.setMethodName("methodName");
					e.setFileName("fileName");
					e.setLineNumber(-1);
					e.setCodeLocation("codeLocation");
					e.setVersion("version");
					e.setExact(false);
				}),
				extendedElement(e -> {
					e.setClassName("className");
					e.setMethodName("methodName2");
					e.setFileName("fileName");
					e.setLineNumber(-1);
					e.setCodeLocation("codeLocation");
					e.setVersion("version");
					e.setExact(false);
				})
			})),
			throwable(info -> info.setCause(throwable(ci -> ci.setName("cause")))),
			throwable(info -> info.setSuppressed(new ThrowableInfo[]{
				throwable(ci -> ci.setName("suppressed1")),
				throwable(ci -> ci.setName("suppressed2"))
			})),
			throwable(info -> {
				info.setName("name");
				info.setMessage("message");
				info.setOmittedElements(3);
				info.setStackTrace(new ExtendedStackTraceElement[]{
					extendedElement(e -> {
						e.setClassName("className");
						e.setMethodName("methodName1");
						e.setFileName("fileName");
						e.setLineNumber(-1);
						e.setCodeLocation("codeLocation");
						e.setVersion("version");
						e.setExact(false);
					}),
					extendedElement(e -> {
						e.setClassName("className");
						e.setMethodName("methodName2");
						e.setFileName("fileName");
						e.setLineNumber(-1);
						e.setCodeLocation("codeLocation");
						e.setVersion("version");
						e.setExact(false);
					})
				});
				info.setCause(throwable(ci -> ci.setName("cause")));
				info.setSuppressed(new ThrowableInfo[]{
					throwable(ci -> ci.setName("suppressed1")),
					throwable(ci -> ci.setName("suppressed2"))
				});
			})
		);
	}

	private static Stream<ThrowableInfo> brokenInstances() {
		ThrowableInfo recursiveCause = new ThrowableInfo();
		recursiveCause.setCause(recursiveCause);

		ThrowableInfo recursiveSuppressed = new ThrowableInfo();
		recursiveSuppressed.setSuppressed(new ThrowableInfo[]{recursiveSuppressed});

		ThrowableInfo nullSuppressed1 = throwable(info -> info.setSuppressed(new ThrowableInfo[]{null}));
		ThrowableInfo nullSuppressed2 = throwable(info -> info.setSuppressed(new ThrowableInfo[]{
			new ThrowableInfo(), null, new ThrowableInfo()
		}));

		ThrowableInfo nullStack1 = throwable(info -> info.setStackTrace(new ExtendedStackTraceElement[]{null}));
		ThrowableInfo nullStack2 = throwable(info -> info.setStackTrace(new ExtendedStackTraceElement[]{
			new ExtendedStackTraceElement(), null, new ExtendedStackTraceElement()
		}));

		return Stream.of(recursiveCause, recursiveSuppressed, nullSuppressed1, nullSuppressed2, nullStack1, nullStack2);
	}

	private static Stream<Arguments> inequalityPairs() {
		return Stream.of(
			Arguments.of(new ThrowableInfo(), throwable(info -> info.setName("b"))),
			Arguments.of(throwable(info -> info.setName("a")), throwable(info -> info.setName("b"))),
			Arguments.of(new ThrowableInfo(), throwable(info -> info.setMessage("b"))),
			Arguments.of(throwable(info -> info.setMessage("a")), throwable(info -> info.setMessage("b"))),
			Arguments.of(new ThrowableInfo(), throwable(info -> info.setCause(new ThrowableInfo()))),
			Arguments.of(new ThrowableInfo(), throwable(info -> info.setSuppressed(new ThrowableInfo[0]))),
			Arguments.of(throwable(info -> info.setSuppressed(new ThrowableInfo[0])),
				throwable(info -> info.setSuppressed(new ThrowableInfo[]{null}))),
			Arguments.of(throwable(info -> info.setSuppressed(new ThrowableInfo[]{null})),
				throwable(info -> info.setSuppressed(new ThrowableInfo[]{new ThrowableInfo()})))
		);
	}

	private static List<String> parseInputValues() {
		List<String> values = new ArrayList<>();
		values.add(
			"java.lang.RuntimeException" + LINE_SEPARATOR +
				"\tat sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)" + LINE_SEPARATOR +
				"\tat sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)" + LINE_SEPARATOR +
				"\tat sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)" + LINE_SEPARATOR
		);

		values.add(
			"java.lang.RuntimeException: Outer" + LINE_SEPARATOR +
				"\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:78)" + LINE_SEPARATOR +
				"\tSuppressed: java.lang.RuntimeException: Suppressed1" + LINE_SEPARATOR +
				"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:80)" + LINE_SEPARATOR +
				"\tSuppressed: java.lang.RuntimeException: Suppressed2" + LINE_SEPARATOR +
				"\t\tat de.huxhorn.lilith.sandbox.Log4jSandbox.main(Log4jSandbox.java:81)" + LINE_SEPARATOR +
				"Caused by: java.lang.RuntimeException: Cause" + LINE_SEPARATOR +
				"\t... 1 more" + LINE_SEPARATOR
		);

		values.add(
			"java.lang.RuntimeException: Hi." + LINE_SEPARATOR +
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
				"\t\t... 2 more" + LINE_SEPARATOR
		);

		return values;
	}

	private static ExtendedStackTraceElement extendedElement(Consumer<ExtendedStackTraceElement> customizer) {
		ExtendedStackTraceElement element = new ExtendedStackTraceElement();
		customizer.accept(element);
		return element;
	}

	private static ThrowableInfo throwable(Consumer<ThrowableInfo> customizer) {
		ThrowableInfo info = new ThrowableInfo();
		customizer.accept(info);
		return info;
	}

	private static void assertThrowableCopy(ThrowableInfo expected, ThrowableInfo actual) {
		assertEquals(expected, actual);
		if(expected == null) {
			return;
		}
		assertNotSame(expected, actual);
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getMessage(), actual.getMessage());
		assertEquals(expected.getOmittedElements(), actual.getOmittedElements());

		ExtendedStackTraceElement[] expectedStack = expected.getStackTrace();
		ExtendedStackTraceElement[] actualStack = actual.getStackTrace();
		if(expectedStack == null) {
			assertNull(actualStack);
		} else {
			assertNotNull(actualStack);
			assertEquals(expectedStack.length, actualStack.length);
			for(int i = 0; i < expectedStack.length; i++) {
				assertEquals(expectedStack[i], actualStack[i]);
				if(expectedStack[i] != null) {
					assertNotSame(expectedStack[i], actualStack[i]);
				}
			}
		}

		ThrowableInfo expectedCause = expected.getCause();
		ThrowableInfo actualCause = actual.getCause();
		if(expectedCause == null) {
			assertNull(actualCause);
		} else {
			assertNotNull(actualCause);
			assertEquals(expectedCause, actualCause);
			assertNotSame(expectedCause, actualCause);
		}

		ThrowableInfo[] expectedSuppressed = expected.getSuppressed();
		ThrowableInfo[] actualSuppressed = actual.getSuppressed();
		if(expectedSuppressed == null) {
			assertNull(actualSuppressed);
		} else {
			assertNotNull(actualSuppressed);
			assertEquals(expectedSuppressed.length, actualSuppressed.length);
			for(int i = 0; i < expectedSuppressed.length; i++) {
				ThrowableInfo expectedEntry = expectedSuppressed[i];
				ThrowableInfo actualEntry = actualSuppressed[i];
				assertEquals(expectedEntry, actualEntry);
				if(expectedEntry != null) {
					assertNotSame(expectedEntry, actualEntry);
				}
			}
		}
	}

	private static void assertNotEqualsSymmetric(Object left, Object right) {
		if(left != null) {
			assertNotEquals(left, right);
		}
		if(right != null) {
			assertNotEquals(right, left);
		}
	}
}
