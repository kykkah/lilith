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

package de.huxhorn.lilith.engine.impl.eventproducer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.classic.spi.LoggingEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

class SerializableWhitelistTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializableEventProducer.class);

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("validValues")
    void deserializationWorksForValidValues(String valueClass, Serializable value) {
        assertDoesNotThrow(() -> writeAndRead(value), valueClass);
    }

    private static Stream<Arguments> validValues() {
        List<Serializable> values = new ArrayList<>();
        values.add("Foo");

        LoggingEvent loggingEvent = new LoggingEvent(
                "foo.bar",
                (ch.qos.logback.classic.Logger) LOGGER,
                Level.DEBUG,
                "message {}",
                new Throwable(),
                new Object[] {new File(".")});
        MDC.put("MDK-Key", "MDC-Value");
        loggingEvent.prepareForDeferredProcessing();
        values.add(LoggingEventVO.build(loggingEvent));
        MDC.clear();

        return values.stream()
                .map(value -> Arguments.of(value.getClass().getName(), value));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("invalidValues")
    void deserializationFailsForDisallowedValues(String valueClass, Serializable value) {
        ClassNotFoundException ex = assertThrows(
                ClassNotFoundException.class,
                () -> readWithWhitelist(value),
                valueClass);
        assertTrue(ex.getMessage().contains("Unauthorized deserialization attempt!"), valueClass);
    }

    private static Stream<Arguments> invalidValues() {
        List<Serializable> values = new ArrayList<>();
        values.add(new File("."));

        return values.stream()
                .map(value -> Arguments.of(value.getClass().getName(), value));
    }

    private static void readWithWhitelist(Serializable serializable) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(serializable);
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                WhitelistObjectInputStream ois =
                        new WhitelistObjectInputStream(bis, SerializableWhitelist.WHITELIST)) {
            ois.readObject();
        }
    }

    private static void writeAndRead(Serializable serializable) throws IOException, ClassNotFoundException {
        readWithWhitelist(serializable);
    }
}
