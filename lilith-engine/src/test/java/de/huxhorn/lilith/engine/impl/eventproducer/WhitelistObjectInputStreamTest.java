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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WhitelistObjectInputStreamTest {

    @Test
    void blockedDryRunningWorksAsExpected() throws Exception {
        SerializableSample foo = new SerializableSample("bar");
        byte[] bytes = serialize(foo);

        Set<String> whitelist = new HashSet<>();
        try (WhitelistObjectInputStream instance =
                new WhitelistObjectInputStream(new ByteArrayInputStream(bytes), whitelist, false, true)) {
            Object read = instance.readObject();

            assertTrue(instance.isDryRunning());
            assertEquals(foo, read);
            assertTrue(instance.getUnauthorized().contains(SerializableSample.class.getName()));
        }
    }

    @Test
    void unblockedDryRunningWorksAsExpected() throws Exception {
        SerializableSample foo = new SerializableSample("bar");
        byte[] bytes = serialize(foo);

        Set<String> whitelist = new HashSet<>();
        whitelist.add(SerializableSample.class.getName());
        try (WhitelistObjectInputStream instance =
                new WhitelistObjectInputStream(new ByteArrayInputStream(bytes), whitelist, false, true)) {
            Object read = instance.readObject();

            assertTrue(instance.isDryRunning());
            assertEquals(foo, read);
            assertFalse(instance.getUnauthorized().contains(SerializableSample.class.getName()));
        }
    }

    @Test
    void blockedWorksAsExpected() throws Exception {
        SerializableSample foo = new SerializableSample("bar");
        byte[] bytes = serialize(foo);

        Set<String> whitelist = new HashSet<>();
        try (WhitelistObjectInputStream instance =
                new WhitelistObjectInputStream(new ByteArrayInputStream(bytes), whitelist)) {
            ClassNotFoundException ex = assertThrows(ClassNotFoundException.class, instance::readObject);
            assertEquals("Unauthorized deserialization attempt! " + SerializableSample.class.getName(), ex.getMessage());
            assertTrue(instance.getUnauthorized().contains(SerializableSample.class.getName()));
            assertFalse(instance.isDryRunning());
        }
    }

    @Test
    void unblockedWorksAsExpected() throws Exception {
        SerializableSample foo = new SerializableSample("bar");
        byte[] bytes = serialize(foo);

        Set<String> whitelist = new HashSet<>();
        whitelist.add(SerializableSample.class.getName());
        try (WhitelistObjectInputStream instance =
                new WhitelistObjectInputStream(new ByteArrayInputStream(bytes), whitelist)) {
            Object read = instance.readObject();

            assertFalse(instance.isDryRunning());
            assertEquals(foo, read);
            assertFalse(instance.getUnauthorized().contains(SerializableSample.class.getName()));
        }
    }

    @Test
    void copySetTrueWorksAsExpected() throws Exception {
        SerializableSample foo = new SerializableSample("bar");
        byte[] bytes = serialize(foo);
        Set<String> whitelist = new HashSet<>(Arrays.asList(SerializableSample.class.getName(), "Something"));

        try (WhitelistObjectInputStream instance =
                new WhitelistObjectInputStream(new ByteArrayInputStream(bytes), whitelist, true)) {
            whitelist.remove("Something");

            assertTrue(instance.getWhitelist().contains("Something"));
            assertTrue(instance.getWhitelist().contains(SerializableSample.class.getName()));
        }
    }

    @Test
    void copySetFalseWorksAsExpected() throws Exception {
        SerializableSample foo = new SerializableSample("bar");
        byte[] bytes = serialize(foo);
        Set<String> whitelist = new HashSet<>(Arrays.asList(SerializableSample.class.getName(), "Something"));

        try (WhitelistObjectInputStream instance =
                new WhitelistObjectInputStream(new ByteArrayInputStream(bytes), whitelist, false)) {
            whitelist.remove("Something");

            assertFalse(instance.getWhitelist().contains("Something"));
            assertTrue(instance.getWhitelist().contains(SerializableSample.class.getName()));
        }
    }

    private static byte[] serialize(Serializable serializable) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(serializable);
        }
        return bos.toByteArray();
    }

    private static final class SerializableSample implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String name;

        private SerializableSample(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SerializableSample)) {
                return false;
            }
            SerializableSample other = (SerializableSample) o;
            return Objects.equals(name, other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
