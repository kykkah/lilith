/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.huxhorn.lilith.engine.xml.eventproducer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LilithXmlStreamLoggingEventProducerTest {

	@Test
	void correctInputFactoryIsObtained() {
		String factoryClassName = LilithXmlStreamLoggingEventProducer.XML_INPUT_FACTORY.getClass().getName();
		assertTrue(factoryClassName.startsWith("com.ctc.wstx.stax"),
			"Factory is expected to be provided by Woodstox");
	}
}
