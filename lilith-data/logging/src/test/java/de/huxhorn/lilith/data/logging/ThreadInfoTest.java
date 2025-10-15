/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
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
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ThreadInfoTest {

	@ParameterizedTest
	@MethodSource("inputValuesIncludingEmpty")
	void serializationWorks(ThreadInfo inputValue) throws Exception {
		ThreadInfo other = testSerialization(inputValue);

		assertEquals(inputValue, other);
		assertEquals(inputValue.hashCode(), other.hashCode());
		assertNotSame(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("inputValuesIncludingEmpty")
	void xmlSerializationWorks(ThreadInfo inputValue) throws Exception {
		ThreadInfo other = testXmlSerialization(inputValue);

		assertEquals(inputValue, other);
		assertEquals(inputValue.hashCode(), other.hashCode());
		assertNotSame(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("inputValuesIncludingEmpty")
	void cloningWorks(ThreadInfo inputValue) throws Exception {
		ThreadInfo other = testClone(inputValue);

		assertEquals(inputValue, other);
		assertEquals(inputValue.hashCode(), other.hashCode());
		assertNotSame(inputValue, other);
	}

	@ParameterizedTest
	@MethodSource("inputValues")
	void equalsBehavesAsExpected(ThreadInfo inputValue) {
		ThreadInfo empty = new ThreadInfo();

	assertEquals(inputValue, inputValue);
	assertNotEquals(inputValue, null);
	assertNotEquals(inputValue, new Object());
	assertNotEquals(inputValue, empty);
	assertNotEquals(empty, inputValue);
	}

	private static Stream<ThreadInfo> inputValues() {
		return Stream.of(
			threadInfo(builder -> builder.setId(17L)),
			threadInfo(builder -> builder.setName("threadName")),
			threadInfo(builder -> builder.setGroupId(17L)),
			threadInfo(builder -> builder.setGroupName("groupName")),
			threadInfo(builder -> builder.setPriority(17))
		);
	}

	private static Stream<ThreadInfo> inputValuesIncludingEmpty() {
		return Stream.concat(inputValues(), Stream.of(new ThreadInfo()));
	}

	private static ThreadInfo threadInfo(java.util.function.Consumer<ThreadInfo> customizer) {
		ThreadInfo info = new ThreadInfo();
		customizer.accept(info);
		return info;
	}
}
