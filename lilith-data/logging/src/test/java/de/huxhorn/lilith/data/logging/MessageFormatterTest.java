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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MessageFormatterTest {

	private static final Throwable SOME_THROWABLE = new MessageFormatterUseCases.FooThrowable("FooException");

	@ParameterizedTest
	@MethodSource("argumentResultValues")
	void argumentResultEqualsBehavesAsExpected(MessageFormatter.ArgumentResult inputValue) {
		MessageFormatter.ArgumentResult empty = new MessageFormatter.ArgumentResult(null, null);

		assertEquals(inputValue, inputValue);
		assertNotEquals(inputValue, null);
		assertNotEquals(inputValue, new Object());
		assertNotEquals(inputValue, empty);
		assertNotEquals(empty, inputValue);
	}

	@ParameterizedTest
	@MethodSource("argumentResultPairs")
	void argumentResultHashCodeBehavesAsExpected(MessageFormatter.ArgumentResult inputValue,
		MessageFormatter.ArgumentResult otherInputValue) {
		assertEquals(inputValue, otherInputValue);
		assertNotSame(inputValue, otherInputValue);
		assertEquals(inputValue.hashCode(), otherInputValue.hashCode());
	}

	@Test
	void argumentResultStoresArgumentsAndThrowable() {
		String[] arguments = new String[]{"foo"};
		ExceptionWithEqualsAndHashCode throwable = new ExceptionWithEqualsAndHashCode("bar");

		MessageFormatter.ArgumentResult instance = new MessageFormatter.ArgumentResult(arguments, throwable);

		assertSame(arguments, instance.getArguments());
		assertSame(throwable, instance.getThrowable());
	}

	@ParameterizedTest
	@MethodSource("formatCases")
	void formatReturnsExpectedResult(String messagePattern, String[] arguments, String expectedResult) {
		assertEquals(expectedResult, MessageFormatter.format(messagePattern, arguments));
	}

	@ParameterizedTest
	@MethodSource("placeholderCountCases")
	void countArgumentPlaceholdersReturnsExpectedResult(String messagePattern, int expectedResult) {
		assertEquals(expectedResult, MessageFormatter.countArgumentPlaceholders(messagePattern));
	}

	@ParameterizedTest
	@MethodSource("evaluateArgumentsUseCases")
	void evaluateArgumentsReturnsExpectedResult(MessageFormatterUseCases.UseCase useCase) {
		MessageFormatter.ArgumentResult result =
			MessageFormatter.evaluateArguments(useCase.getMessagePattern(), useCase.getArguments());
		assertArgumentResultEquals(useCase.getArgumentResult(), result);
	}

	@ParameterizedTest
	@MethodSource("formatUseCases")
	void formatUseCasesReturnExpectedResult(MessageFormatterUseCases.UseCase useCase) {
		assertEquals(useCase.getExpectedResult(),
			MessageFormatter.format(useCase.getMessagePattern(), useCase.getArgumentStrings()));
	}

	@ParameterizedTest
	@MethodSource("evaluateArgumentsCases")
	void evaluateArgumentsTableMatchesExpectations(String messagePattern, Object[] arguments,
		MessageFormatter.ArgumentResult expectedResult) {
		MessageFormatter.ArgumentResult result = MessageFormatter.evaluateArguments(messagePattern, arguments);
		assertArgumentResultEquals(expectedResult, result);
	}

	@ParameterizedTest
	@MethodSource("formatEvaluationCases")
	void formatEvaluationProducesExpectedOutput(String messagePattern, Object[] arguments,
		Throwable expectedThrowable, String expectedResult) {
		MessageFormatter.ArgumentResult argumentResult = MessageFormatter.evaluateArguments(messagePattern, arguments);
		String result = MessageFormatter.format(messagePattern, argumentResult == null ? null : argumentResult.getArguments());

		assertEquals(expectedResult, result);
		if(argumentResult == null) {
			assertNull(expectedThrowable);
		} else {
			if(expectedThrowable == null) {
				assertNull(argumentResult.getThrowable());
			} else {
				assertEquals(String.valueOf(expectedThrowable), String.valueOf(argumentResult.getThrowable()));
			}
		}
	}

	@Test
	void exceptionEqualsAndHashCode() {
		ExceptionWithEqualsAndHashCode foo1 = new ExceptionWithEqualsAndHashCode("foo");
		ExceptionWithEqualsAndHashCode foo2 = new ExceptionWithEqualsAndHashCode("foo");
		ExceptionWithEqualsAndHashCode bar = new ExceptionWithEqualsAndHashCode("bar");

		assertEquals(foo1, foo2);
		assertEquals(foo1.hashCode(), foo2.hashCode());
		assertNotEquals(foo1, bar);
		assertNotEquals(foo1.hashCode(), bar.hashCode());
	}

	@Test
	void defaultConstructorCoverage() throws Exception {
		Constructor<MessageFormatter> constructor = MessageFormatter.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	private static Stream<MessageFormatter.ArgumentResult> argumentResultValues() {
		return Stream.of(
			new MessageFormatter.ArgumentResult(new String[0], null),
			new MessageFormatter.ArgumentResult(new String[]{"first", "second"}, null),
			new MessageFormatter.ArgumentResult(new String[]{"first", null, "third"}, null),
			new MessageFormatter.ArgumentResult(null, new ExceptionWithEqualsAndHashCode("foo")),
			new MessageFormatter.ArgumentResult(new String[0], new ExceptionWithEqualsAndHashCode("foo")),
			new MessageFormatter.ArgumentResult(new String[]{"first", "second"}, new ExceptionWithEqualsAndHashCode("foo")),
			new MessageFormatter.ArgumentResult(new String[]{"first", null, "third"}, new ExceptionWithEqualsAndHashCode("foo"))
		);
	}

	private static Stream<Arguments> argumentResultPairs() {
		return Stream.of(
			pair(argResult(new String[0], null)),
			pair(argResult(new String[]{"first", "second"}, null)),
			pair(argResult(new String[]{"first", null, "third"}, null)),
			pair(argResult(null, new ExceptionWithEqualsAndHashCode("foo"))),
			pair(argResult(new String[0], new ExceptionWithEqualsAndHashCode("foo"))),
			pair(argResult(new String[]{"first", "second"}, new ExceptionWithEqualsAndHashCode("foo"))),
			pair(argResult(new String[]{"first", null, "third"}, new ExceptionWithEqualsAndHashCode("foo"))),
			pair(new MessageFormatter.ArgumentResult(null, null))
		);
	}

	private static Arguments pair(MessageFormatter.ArgumentResult value) {
		return Arguments.of(value, cloneArgumentResult(value));
	}

	private static MessageFormatter.ArgumentResult cloneArgumentResult(MessageFormatter.ArgumentResult original) {
		return argResult(copyArray(original.getArguments()), original.getThrowable());
	}

	private static String[] copyArray(String[] source) {
		return source == null ? null : Arrays.copyOf(source, source.length);
	}

	private static MessageFormatter.ArgumentResult argResult(String[] arguments, Throwable throwable) {
		return new MessageFormatter.ArgumentResult(arguments, throwable);
	}

	private static Stream<Arguments> formatCases() {
		return Stream.of(
			Arguments.of(null, null, null),
			Arguments.of("foo {}", null, "foo {}"),
			Arguments.of("foo {}", new String[0], "foo {}"),
			Arguments.of("{} {}", new String[]{"foo"}, "foo {}")
		);
	}

	private static Stream<Arguments> placeholderCountCases() {
		return Stream.of(
			Arguments.of(null, 0),
			Arguments.of("foo", 0),
			Arguments.of("{}", 1),
			Arguments.of("{} {} {}", 3),
			Arguments.of("{", 0),
			Arguments.of("{} { {}", 2),
			Arguments.of("{} {", 1),
			Arguments.of("\\{}", 0),
			Arguments.of("\\\\{}", 1),
			Arguments.of("\\\\\\{}", 0),
			Arguments.of("{} \\{}", 1),
			Arguments.of("{} \\\\{}", 2),
			Arguments.of("{} \\\\\\{}", 1)
		);
	}

	private static Stream<MessageFormatterUseCases.UseCase> evaluateArgumentsUseCases() {
		return Stream.of(MessageFormatterUseCases.generateUseCases());
	}

	private static Stream<MessageFormatterUseCases.UseCase> formatUseCases() {
		return Stream.of(MessageFormatterUseCases.generateUseCases());
	}

	private static Stream<Arguments> evaluateArgumentsCases() {
		return Stream.of(
			Arguments.of(null, null, null),
			Arguments.of("{}{}{}", new Object[]{"foo", null, 1L}, argResult(new String[]{"foo", "null", "1"}, null)),
			Arguments.of("{}{}", new Object[]{"foo", null, SOME_THROWABLE}, argResult(new String[]{"foo", "null"}, SOME_THROWABLE)),
			Arguments.of("{}{}{}", new Object[]{"foo", null, SOME_THROWABLE}, argResult(new String[]{"foo", "null", "FooException"}, null)),
			Arguments.of("{}{}{}", new Object[]{"foo", null, SOME_THROWABLE, 17L, 18L},
				argResult(new String[]{"foo", "null", "FooException", "17", "18"}, null)),
			Arguments.of("{}{}{}", new Object[]{"foo", null, 17L, 18L, SOME_THROWABLE},
				argResult(new String[]{"foo", "null", "17", "18"}, SOME_THROWABLE))
		);
	}

	private static Stream<Arguments> formatEvaluationCases() {
		return Stream.of(
			Arguments.of("param1={}, param2={}, param3={}",
				new Object[]{"One", "Two", "Three", new RuntimeException()},
				new RuntimeException(),
				"param1=One, param2=Two, param3=Three"),
			Arguments.of("param1={}, param2={}, param3={}, exceptionString={}",
				new Object[]{"One", "Two", "Three", new RuntimeException()},
				null,
				"param1=One, param2=Two, param3=Three, exceptionString=java.lang.RuntimeException"),
			Arguments.of("exceptionString={}",
				new Object[]{new RuntimeException()},
				null,
				"exceptionString=java.lang.RuntimeException"),
			Arguments.of("param={}",
				new Object[]{new Object[]{"One", "Two", "Three"}},
				null,
				"param=['One', 'Two', 'Three']"),
			Arguments.of("param1={}, param2={}, param3={}",
				new Object[]{"One", "Two", "Three", "Unused", "Unused", new RuntimeException()},
				new RuntimeException(),
				"param1=One, param2=Two, param3=Three"),
			Arguments.of("param1={}, param2={}, param3={}",
				new Object[]{"One", new String[]{"Two.1", "Two.2"}, "Three"},
				null,
				"param1=One, param2=['Two.1', 'Two.2'], param3=Three")
		);
	}

	private static void assertArgumentResultEquals(MessageFormatter.ArgumentResult expected,
		MessageFormatter.ArgumentResult actual) {
		if(expected == null) {
			assertNull(actual);
			return;
		}
		assertNotNull(actual);
		assertArrayEquals(expected.getArguments(), actual.getArguments());
		if(expected.getThrowable() == null) {
			assertNull(actual.getThrowable());
		} else {
			assertEquals(expected.getThrowable(), actual.getThrowable());
		}
	}

	private static class ExceptionWithEqualsAndHashCode extends RuntimeException {
		private final String value;

		private ExceptionWithEqualsAndHashCode(String value) {
			super(value);
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) {
				return true;
			}
			if(o == null || getClass() != o.getClass()) {
				return false;
			}
			ExceptionWithEqualsAndHashCode that = (ExceptionWithEqualsAndHashCode) o;
			return value != null ? value.equals(that.value) : that.value == null;
		}

		@Override
		public int hashCode() {
			return value != null ? value.hashCode() : 0;
		}
	}
}
