package de.huxhorn.lilith.services.clipboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClipboardFormatterDataTest {

    @ParameterizedTest
    @MethodSource("equalsAndHashCodeArguments")
    void equalsAndHashCodeWorkAsExpected(
            ClipboardFormatterData lhs, ClipboardFormatterData rhs, boolean expectEqual) {
        if (expectEqual) {
            assertEquals(lhs, rhs);
            assertEquals(rhs, lhs);
            assertEquals(lhs.hashCode(), rhs.hashCode());
        } else {
            assertNotEquals(lhs, rhs);
            assertNotEquals(rhs, lhs);
        }

        // sanity checks copied from the Spock spec
        assertEquals(lhs, lhs);
        assertNotEquals(lhs, null);
        assertNotEquals(lhs, Integer.valueOf(17));
    }

    @Test
    void constructorRejectsNullFormatter() {
        NullPointerException exception =
                assertThrows(NullPointerException.class, () -> new ClipboardFormatterData(null));
        assertEquals("clipboardFormatter must not be null!", exception.getMessage());
    }

    @Test
    void gettersExposeFormatterState() {
        FooFormatter formatter = new FooFormatter("fooName", "fooDescription", "fooAccelerator");
        ClipboardFormatterData instance = new ClipboardFormatterData(formatter);

        assertEquals("fooName", instance.getName());
        assertEquals("fooDescription", instance.getDescription());
        assertEquals("fooAccelerator", instance.getAccelerator());
    }

    static Stream<Arguments> equalsAndHashCodeArguments() {
        ClipboardFormatterData empty =
                new ClipboardFormatterData(new FooFormatter(null, null, null));

        return Stream.of(
                Arguments.of(empty, new ClipboardFormatterData(new FooFormatter(null, null, null)), true),
                Arguments.of(
                        new ClipboardFormatterData(new FooFormatter("name", null, null)),
                        new ClipboardFormatterData(new FooFormatter("name", null, null)),
                        true),
                Arguments.of(
                        new ClipboardFormatterData(new FooFormatter("name", null, null)), empty, false),
                Arguments.of(
                        new ClipboardFormatterData(new FooFormatter(null, "description", null)),
                        new ClipboardFormatterData(new FooFormatter(null, "description", null)),
                        true),
                Arguments.of(
                        new ClipboardFormatterData(new FooFormatter(null, "description", null)),
                        empty,
                        false),
                Arguments.of(
                        new ClipboardFormatterData(new FooFormatter(null, null, "accelerator")),
                        new ClipboardFormatterData(new FooFormatter(null, null, "accelerator")),
                        true),
                Arguments.of(
                        new ClipboardFormatterData(new FooFormatter(null, null, "accelerator")),
                        empty,
                        false));
    }

    private static final class FooFormatter implements ClipboardFormatter {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final String description;
        private final String accelerator;

        private FooFormatter(String name, String description, String accelerator) {
            this.name = name;
            this.description = description;
            this.accelerator = accelerator;
        }

        @Override
        public boolean isCompatible(Object object) {
            return false;
        }

        @Override
        public String toString(Object object) {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getAccelerator() {
            return accelerator;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, description, accelerator);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FooFormatter)) {
                return false;
            }
            FooFormatter other = (FooFormatter) obj;
            return Objects.equals(name, other.name)
                    && Objects.equals(description, other.description)
                    && Objects.equals(accelerator, other.accelerator);
        }

        @Override
        public Integer getMnemonic() {
            return ClipboardFormatter.super.getMnemonic();
        }

        @Override
        public boolean isNative() {
            return ClipboardFormatter.super.isNative();
        }
    }
}
