/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2017 Joern Huxhorn
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
 * Copyright 2007-2017 Joern Huxhorn
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExtendedStackTraceElementTest {

	private static final boolean IS_AT_LEAST_JAVA_9 = Runtime.version().feature() >= 9;


	@ParameterizedTest
	@MethodSource("parseExamples")
	void parseStackTraceElementExamples(String input, ExtendedStackTraceElement expected) {
		ExtendedStackTraceElement parsed = ExtendedStackTraceElement.parseStackTraceElement(input);
		assertEquals(expected, parsed);
	}

	static Stream<Arguments> parseExamples() {
		return Stream.of(
			parseArg(null, null),
			parseArg("foo", null),
			parseArg("methodName(Unknown Source)", null),
			parseArg("java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:303", null),
			parseArg("java.util.concurrent.FutureTask$Sync.innerRun(:)", null),
			parseArg("java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:foo)", null),

			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [lilith.jar:0.9.35-SNAPSHOT",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ~[lilith.jar:0.9.35-SNAPSHOT",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079)  ",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079)   ",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) lilith.jar:0.9.35-SNAPSHOT]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [lilith.jar]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager",
					"MainFrame.java", 1079, null, null, null, null, null, null)),

			parseArg(".(Unknown Source)",
				expected("", "", null, ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER, null, null, null, null, null, null)),
			parseArg("className.methodName(Unknown Source)",
				expected("className", "methodName", null, ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER, null, null, null, null, null, null)),
			parseArg("java.lang.Thread.sleep(Native Method)",
				expected("java.lang.Thread", "sleep", null, ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER, null, null, null, null, null, null)),
			parseArg("java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:303)",
				expected("java.util.concurrent.FutureTask$Sync", "innerRun", "FutureTask.java", 303, null, null, null, null, null, null)),
			parseArg("java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java)",
				expected("java.util.concurrent.FutureTask$Sync", "innerRun", "FutureTask.java",
					ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER, null, null, null, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [lilith.jar:0.9.35-SNAPSHOT]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					"lilith.jar", "0.9.35-SNAPSHOT", Boolean.TRUE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ~[lilith.jar:0.9.35-SNAPSHOT]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					"lilith.jar", "0.9.35-SNAPSHOT", Boolean.FALSE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [:0.9.35-SNAPSHOT]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					null, "0.9.35-SNAPSHOT", Boolean.TRUE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ~[:0.9.35-SNAPSHOT]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					null, "0.9.35-SNAPSHOT", Boolean.FALSE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [lilith.jar:]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					"lilith.jar", null, Boolean.TRUE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ~[lilith.jar:]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					"lilith.jar", null, Boolean.FALSE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [:]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					null, null, Boolean.TRUE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ~[:]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					null, null, Boolean.FALSE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) [na:na]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					null, null, Boolean.TRUE, null, null, null)),
			parseArg("de.huxhorn.lilith.swing.MainFrame.setAccessEventSourceManager(MainFrame.java:1079) ~[na:na]",
				expected("de.huxhorn.lilith.swing.MainFrame", "setAccessEventSourceManager", "MainFrame.java", 1079,
					null, null, Boolean.FALSE, null, null, null)),
			parseArg("com.foo.loader/foo@9.0/com.foo.Main.run(Main.java:101)",
				expected("com.foo.Main", "run", "Main.java", 101, null, null, Boolean.FALSE,
					"com.foo.loader", "foo", "9.0")),
			parseArg("com.foo.loader/foo@9.0/com.foo.Main.run(Main.java)",
				expected("com.foo.Main", "run", "Main.java", ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER, null, null, Boolean.FALSE,
					"com.foo.loader", "foo", "9.0")),
			parseArg("com.foo.loader/foo@9.0/com.foo.Main.run(Unknown Source)",
				expected("com.foo.Main", "run", null, ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER, null, null, Boolean.FALSE,
					"com.foo.loader", "foo", "9.0")),
			parseArg("com.foo.loader/foo@9.0/com.foo.Main.run(Native Method)",
				expected("com.foo.Main", "run", null, ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER, null, null, Boolean.FALSE,
					"com.foo.loader", "foo", "9.0")),
			parseArg("com.foo.loader//com.foo.bar.App.run(App.java:12)",
				expected("com.foo.bar.App", "run", "App.java", 12, null, null, Boolean.FALSE,
					"com.foo.loader", null, null)),
			parseArg("acme@2.1/org.acme.Lib.test(Lib.java:80)",
				expected("org.acme.Lib", "test", "Lib.java", 80, null, null, Boolean.FALSE,
					null, "acme", "2.1")),
			parseArg("MyClass.mash(MyClass.java:9)",
				expected("MyClass", "mash", "MyClass.java", 9, null, null, Boolean.FALSE,
					null, null, null))
		);
	}

	@ParameterizedTest
	@MethodSource("specialParseExamples")
	void parseSpecialForms(String input, ExtendedStackTraceElement expected) {
		ExtendedStackTraceElement parsed = ExtendedStackTraceElement.parseStackTraceElement(input);
		assertEquals(expected, parsed);
	}

	static Stream<Arguments> specialParseExamples() {
		return Stream.of(
			parseArg("//com.foo.bar.App.run(App.java:12)",
				expected("com.foo.bar.App", "run", "App.java", 12, null, null, Boolean.FALSE,
					null, null, null)),
			parseArg("/@/com.foo.bar.App.run(App.java:12)",
				expected("com.foo.bar.App", "run", "App.java", 12, null, null, Boolean.FALSE,
					null, null, null))
		);
	}

	@ParameterizedTest
	@MethodSource("serializationInputs")
	void serializationRoundTrips(ExtendedStackTraceElement inputValue) throws Exception {
		ExtendedStackTraceElement copy = testSerialization(inputValue);
		assertCloneEquality(inputValue, copy);
	}

	@ParameterizedTest
	@MethodSource("serializationInputs")
	void xmlSerializationRoundTrips(ExtendedStackTraceElement inputValue) throws Exception {
		ExtendedStackTraceElement copy = testXmlSerialization(inputValue);
		assertCloneEquality(inputValue, copy);
	}

	@ParameterizedTest
	@MethodSource("serializationInputs")
	void cloningRoundTrips(ExtendedStackTraceElement inputValue) throws Exception {
		ExtendedStackTraceElement copy = testClone(inputValue);
		assertCloneEquality(inputValue, copy);
	}

	@Test
	void defaultConstructorSerialization() throws Exception {
		ExtendedStackTraceElement inputValue = new ExtendedStackTraceElement();
		ExtendedStackTraceElement copy = testSerialization(inputValue);
		assertCloneEquality(inputValue, copy);
	}

	@Test
	void defaultConstructorXmlSerialization() throws Exception {
		ExtendedStackTraceElement inputValue = new ExtendedStackTraceElement();
		ExtendedStackTraceElement copy = testXmlSerialization(inputValue);
		assertCloneEquality(inputValue, copy);
	}

	@Test
	void defaultConstructorCloning() throws Exception {
		ExtendedStackTraceElement inputValue = new ExtendedStackTraceElement();
		ExtendedStackTraceElement copy = testClone(inputValue);
		assertCloneEquality(inputValue, copy);
	}

	@ParameterizedTest
	@MethodSource("validInputValuesCompatible")
	void plainToStringCompatibility(ExtendedStackTraceElement inputValue) {
		StackTraceElement ste = inputValue.getStackTraceElement();
		assertNotNull(ste);
		assertEquals(ste.toString(), inputValue.toString(false));
	}

	@ParameterizedTest
	@MethodSource("validInputValuesWithModules")
	void extendedToStringCompatibility(ExtendedStackTraceElement inputValue, String expected) {
		assertEquals(expected, inputValue.toString(true));
	}

	@ParameterizedTest
	@MethodSource("validInputValuesExtendedString")
	void getExtendedStringCompatibility(ExtendedStackTraceElement inputValue, String expected) {
		assertEquals(expected, inputValue.getExtendedString());
	}

	@ParameterizedTest
	@MethodSource("serializationInputs")
	void equalsSanity(ExtendedStackTraceElement inputValue) {
		ExtendedStackTraceElement instance = new ExtendedStackTraceElement();
		assertEquals(instance, instance);
		assertNotNull(instance);
		assertNotEquals(instance, null);
		assertNotEquals(instance, new Object());
		assertNotEquals(instance, inputValue);
		assertNotEquals(inputValue, instance);
	}

	@Test
	void constructorWithNullStackTraceElementFails() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> new ExtendedStackTraceElement(null));
		assertEquals("stackTraceElement must not be null!", ex.getMessage());
	}

	@ParameterizedTest
	@MethodSource("stackTraceElementInputs")
	void constructorCopiesStackTraceElement(StackTraceElement input) {
		ExtendedStackTraceElement element = new ExtendedStackTraceElement(input);
		StackTraceElement copy = element.getStackTraceElement();
		assertEquals(input, copy);
		assertNotSame(input, copy);
	}

	@ParameterizedTest
	@MethodSource("missingNameElements")
	void missingClassOrMethodNamePreventsStackTraceElement(ExtendedStackTraceElement input) {
		assertNull(input.getStackTraceElement());
	}

	private static Stream<Arguments> stackTraceElementInputs() {
		return Stream.of(
			Arguments.of(new StackTraceElement("declaringClass", "methodName", "fileName", 17)),
			Arguments.of(new StackTraceElement("declaringClass", "methodName", null, ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER)),
			Arguments.of(new StackTraceElement("declaringClass", "methodName", null, ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER)),
			Arguments.of(new StackTraceElement("declaringClass", "methodName", null, 17)),
			Arguments.of(new StackTraceElement("", "", null, ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER)),
			Arguments.of(new StackTraceElement("", "", null, ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER)),
			Arguments.of(new StackTraceElement("", "", null, 17)),
			Arguments.of(new StackTraceElement("", "", null, -3))
		);
	}

	private static Stream<ExtendedStackTraceElement> missingNameElements() {
		return Stream.of(
			element(e -> {
				e.setClassName(null);
				e.setMethodName(null);
			}),
			element(e -> {
				e.setClassName(null);
				e.setMethodName("methodName");
			}),
			element(e -> {
				e.setClassName("className");
				e.setMethodName(null);
			})
		);
	}

	private static Stream<ExtendedStackTraceElement> serializationInputs() {
		return Stream.of(
			element(e -> e.setClassName("className")),
			element(e -> e.setMethodName("methodName")),
			element(e -> e.setFileName("fileName")),
			element(e -> e.setLineNumber(17)),
			element(e -> e.setLineNumber(ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER)),
			element(e -> e.setCodeLocation("codeLocation")),
			element(e -> e.setVersion("version")),
			element(e -> e.setExact(true)),
			element(e -> e.setClassLoaderName("classLoaderName")),
			element(e -> e.setModuleName("moduleName")),
			element(e -> e.setModuleVersion("moduleVersion")),
			element(e -> {
				e.setClassName("className");
				e.setMethodName("methodName");
				e.setFileName("fileName");
				e.setLineNumber(17);
				e.setCodeLocation("codeLocation");
				e.setVersion("version");
				e.setExact(true);
			}),
			element(e -> {
				e.setClassName("className");
				e.setMethodName("methodName");
				e.setFileName("fileName");
				e.setLineNumber(ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER);
				e.setCodeLocation("codeLocation");
				e.setVersion("version");
				e.setExact(true);
			}),
			element(e -> {
				e.setClassName("className");
				e.setMethodName("methodName");
				e.setFileName("fileName");
				e.setLineNumber(17);
				e.setClassLoaderName("ClassLoaderName");
				e.setModuleName("ModuleName");
				e.setModuleVersion("ModuleVersion");
			})
		);
	}

	private static Stream<ExtendedStackTraceElement> validInputValuesCompatible() {
		return validInputValues(true).stream();
	}

	private static Stream<Arguments> validInputValuesWithModules() {
		List<ExtendedStackTraceElement> values = validInputValues(false);
		List<String> expectedStrings = validInputValueToStringExtendedStrings();
		return IntStream.range(0, values.size())
			.mapToObj(i -> Arguments.of(values.get(i), expectedStrings.get(i)));
	}

	private static Stream<Arguments> validInputValuesExtendedString() {
		List<ExtendedStackTraceElement> values = validInputValues(false);
		List<String> expectedStrings = validInputValueGetExtendedStringStrings();
		return IntStream.range(0, values.size())
			.mapToObj(i -> Arguments.of(values.get(i), expectedStrings.get(i)));
	}

	private static List<ExtendedStackTraceElement> validInputValues(boolean onlyCompatible) {
		List<ExtendedStackTraceElement> result = new ArrayList<>();
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setFileName("fileName");
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setLineNumber(17);
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setCodeLocation("codeLocation");
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setVersion("version");
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setExact(true);
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setFileName("fileName");
			e.setLineNumber(17);
			e.setCodeLocation("codeLocation");
			e.setVersion("version");
			e.setExact(true);
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setFileName("fileName");
			e.setLineNumber(ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER);
			e.setCodeLocation("codeLocation");
			e.setVersion("version");
			e.setExact(true);
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setFileName("fileName");
			e.setLineNumber(ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER);
			e.setCodeLocation("codeLocation");
			e.setVersion("version");
			e.setExact(true);
		}));
		result.add(element(e -> {
			e.setClassName("className");
			e.setMethodName("methodName");
			e.setFileName("fileName");
			e.setLineNumber(ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER);
			e.setCodeLocation("codeLocation");
			e.setVersion("version");
			e.setExact(false);
		}));

		if(!onlyCompatible || IS_AT_LEAST_JAVA_9) {
			result.add(element(e -> {
				e.setClassName("com.foo.Main");
				e.setMethodName("run");
				e.setFileName("Main.java");
				e.setLineNumber(101);
				e.setExact(false);
				e.setClassLoaderName("com.foo.loader");
				e.setModuleName("foo");
				e.setModuleVersion("9.0");
			}));
			result.add(element(e -> {
				e.setClassName("com.foo.Main");
				e.setMethodName("run");
				e.setFileName("Main.java");
				e.setLineNumber(ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER);
				e.setExact(false);
				e.setClassLoaderName("com.foo.loader");
				e.setModuleName("foo");
				e.setModuleVersion("9.0");
			}));
			result.add(element(e -> {
				e.setClassName("com.foo.Main");
				e.setMethodName("run");
				e.setLineNumber(ExtendedStackTraceElement.UNKNOWN_SOURCE_LINE_NUMBER);
				e.setExact(false);
				e.setClassLoaderName("com.foo.loader");
				e.setModuleName("foo");
				e.setModuleVersion("9.0");
			}));
			result.add(element(e -> {
				e.setClassName("com.foo.Main");
				e.setMethodName("run");
				e.setLineNumber(ExtendedStackTraceElement.NATIVE_METHOD_LINE_NUMBER);
				e.setExact(false);
				e.setClassLoaderName("com.foo.loader");
				e.setModuleName("foo");
				e.setModuleVersion("9.0");
			}));
			result.add(element(e -> {
				e.setClassName("com.foo.bar.App");
				e.setMethodName("run");
				e.setFileName("App.java");
				e.setLineNumber(12);
				e.setExact(false);
				e.setClassLoaderName("com.foo.loader");
			}));
			result.add(element(e -> {
				e.setClassName("org.acme.Lib");
				e.setMethodName("test");
				e.setFileName("Lib.java");
				e.setLineNumber(80);
				e.setExact(false);
				e.setModuleName("acme");
				e.setModuleVersion("2.1");
			}));
			result.add(element(e -> {
				e.setClassName("MyClass");
				e.setMethodName("mash");
				e.setFileName("MyClass.java");
				e.setLineNumber(9);
				e.setExact(false);
			}));
			result.add(element(e -> {
				e.setClassName("org.acme.Lib");
				e.setMethodName("test");
				e.setFileName("Lib.java");
				e.setLineNumber(80);
				e.setExact(false);
				e.setClassLoaderName("");
				e.setModuleName("");
				e.setModuleVersion("");
			}));
			result.add(element(e -> {
				e.setClassName("org.acme.Lib");
				e.setMethodName("test");
				e.setFileName("Lib.java");
				e.setLineNumber(80);
				e.setExact(false);
				e.setClassLoaderName("");
				e.setModuleName("foo");
				e.setModuleVersion("");
			}));
		}

		return result;
	}

	private static List<String> validInputValueToStringExtendedStrings() {
		List<String> result = new ArrayList<>(Arrays.asList(
			"className.methodName(Unknown Source)",
			"className.methodName(fileName)",
			"className.methodName(Unknown Source)",
			"className.methodName(Unknown Source) ~[codeLocation:na]",
			"className.methodName(Unknown Source) ~[na:version]",
			"className.methodName(Unknown Source)",
			"className.methodName(fileName:17) [codeLocation:version]",
			"className.methodName(Native Method) [codeLocation:version]",
			"className.methodName(fileName) [codeLocation:version]",
			"className.methodName(fileName) ~[codeLocation:version]"
		));

		if(IS_AT_LEAST_JAVA_9) {
			result.addAll(Arrays.asList(
				"com.foo.loader/foo@9.0/com.foo.Main.run(Main.java:101)",
				"com.foo.loader/foo@9.0/com.foo.Main.run(Main.java)",
				"com.foo.loader/foo@9.0/com.foo.Main.run(Unknown Source)",
				"com.foo.loader/foo@9.0/com.foo.Main.run(Native Method)",
				"com.foo.loader//com.foo.bar.App.run(App.java:12)",
				"acme@2.1/org.acme.Lib.test(Lib.java:80)",
				"MyClass.mash(MyClass.java:9)",
				"org.acme.Lib.test(Lib.java:80)",
				"foo/org.acme.Lib.test(Lib.java:80)"
			));
		}

		return result;
	}

	private static List<String> validInputValueGetExtendedStringStrings() {
		List<String> result = new ArrayList<>(Arrays.asList(
			null,
			null,
			null,
			"~[codeLocation:na]",
			"~[na:version]",
			null,
			"[codeLocation:version]",
			"[codeLocation:version]",
			"[codeLocation:version]",
			"~[codeLocation:version]"
		));

		if(IS_AT_LEAST_JAVA_9) {
			result.addAll(Arrays.asList(
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
			));
		}

		return result;
	}

	private static ExtendedStackTraceElement element(Consumer<ExtendedStackTraceElement> customizer) {
		ExtendedStackTraceElement element = new ExtendedStackTraceElement();
		customizer.accept(element);
		return element;
	}

	private static ExtendedStackTraceElement expected(String className, String methodName, String fileName,
		Integer lineNumber, String codeLocation, String version, Boolean exact,
		String classLoaderName, String moduleName, String moduleVersion) {
		if(className == null && methodName == null && fileName == null && lineNumber == null
			&& codeLocation == null && version == null && exact == null
			&& classLoaderName == null && moduleName == null && moduleVersion == null) {
			return null;
		}
		ExtendedStackTraceElement element = new ExtendedStackTraceElement();
		element.setClassName(className);
		element.setMethodName(methodName);
		element.setFileName(fileName);
		if(lineNumber != null) {
			element.setLineNumber(lineNumber);
		}
		element.setCodeLocation(codeLocation);
		element.setVersion(version);
		if(exact != null) {
			element.setExact(exact);
		}
		element.setClassLoaderName(classLoaderName);
		element.setModuleName(moduleName);
		element.setModuleVersion(moduleVersion);
		return element;
	}

	private static Arguments parseArg(String input, ExtendedStackTraceElement expected) {
		return Arguments.of(input, expected);
	}

	private static void assertCloneEquality(ExtendedStackTraceElement original, ExtendedStackTraceElement copy) {
		assertEquals(original, copy);
		assertNotSame(original, copy);
		if(original != null) {
			assertEquals(original.getClassName(), copy.getClassName());
			assertEquals(original.getMethodName(), copy.getMethodName());
			assertEquals(original.getFileName(), copy.getFileName());
			assertEquals(original.getLineNumber(), copy.getLineNumber());
			assertEquals(original.getCodeLocation(), copy.getCodeLocation());
			assertEquals(original.getVersion(), copy.getVersion());
			assertEquals(original.isExact(), copy.isExact());
			assertEquals(original.getClassLoaderName(), copy.getClassLoaderName());
			assertEquals(original.getModuleName(), copy.getModuleName());
			assertEquals(original.getModuleVersion(), copy.getModuleVersion());
		}
	}
}
