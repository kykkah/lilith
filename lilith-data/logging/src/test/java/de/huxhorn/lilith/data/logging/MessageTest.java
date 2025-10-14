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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MessageTest {

	private static final Message INSTANCE = new Message();

	@Test
	void defaultConstructorWorksAsExpected() {
		Message instance = new Message();

		assertNull(instance.getMessagePattern());
		assertNull(instance.getArguments());
		assertNull(instance.getMessage());
	}

	@Test
	void singleParameterConstructorWorksAsExpected() {
		Message instance = new Message("message");

		assertEquals("message", instance.getMessagePattern());
		assertNull(instance.getArguments());
		assertEquals("message", instance.getMessage());
	}

	@Test
	void twoParameterConstructorWorksAsExpected() {
		Message instance = new Message("message {} {}", new String[]{"one", "two"});

		assertEquals("message {} {}", instance.getMessagePattern());
		assertArrayEquals(new String[]{"one", "two"}, instance.getArguments());
		assertEquals("message one two", instance.getMessage());
		// walking lazy init path 2
		assertEquals("message one two", instance.getMessage());
	}

	@ParameterizedTest
	@MethodSource("someInstances")
	void serializationWorks(Message message) throws Exception {
		Message other = testSerialization(message);
		assertMessageCopy(message, other);
	}

	@ParameterizedTest
	@MethodSource("someInstances")
	void xmlSerializationWorks(Message message) throws Exception {
		Message other = testXmlSerialization(message);
		assertMessageCopy(message, other);
	}

	@ParameterizedTest
	@MethodSource("someInstances")
	void cloningWorks(Message message) throws Exception {
		Message other = testClone(message);
		assertMessageCopy(message, other);
	}

	@ParameterizedTest
	@MethodSource("equalsCases")
	void equalsAndHashCodeWorks(Message instance, Object other, boolean expectedEqual) {
		assertEquals(expectedEqual, instance.equals(other));
		if(expectedEqual && other instanceof Message otherMessage) {
			assertEquals(instance.hashCode(), otherMessage.hashCode());
		}
	}

	private static void assertMessageCopy(Message original, Message copy) {
		assertEquals(original, copy);
		assertEquals(original.hashCode(), copy.hashCode());
		assertNotSame(original, copy);
		assertEquals(original.getMessagePattern(), copy.getMessagePattern());
		assertEquals(original.getMessage(), copy.getMessage());

		String[] originalArgs = original.getArguments();
		String[] copyArgs = copy.getArguments();
		if(originalArgs == null) {
			assertNull(copyArgs);
		} else {
			assertNotNull(copyArgs);
			assertArrayEquals(originalArgs, copyArgs);
			assertNotSame(originalArgs, copyArgs);
		}
	}

	private static Stream<Message> someInstances() {
		return Stream.of(
			new Message(),
			new Message("message"),
			new Message("messagePattern", new String[]{"one", "two"}),
			new Message("messagePattern {} {}", new String[]{"one", "two"}),
			new Message("messagePattern", new String[]{"one", null, "three"})
		);
	}

	private static Stream<Arguments> equalsCases() {
		return Stream.of(
			Arguments.of(INSTANCE, INSTANCE, true),
			Arguments.of(INSTANCE, "foo", false),
			Arguments.of(INSTANCE, null, false),
			Arguments.of(new Message(), new Message(), true),
			Arguments.of(new Message(), new Message("message"), false),
			Arguments.of(new Message("message"), new Message("message"), true),
			Arguments.of(new Message("message"), new Message(), false),
			Arguments.of(new Message("message"), new Message("message2"), false),
			Arguments.of(new Message("", new String[]{"a"}), new Message("", new String[]{"a"}), true),
			Arguments.of(new Message("", new String[]{"a"}), new Message("", new String[]{"b"}), false)
		);
	}
}
