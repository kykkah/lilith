/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionBundleTest {

    @ParameterizedTest(name = "{0} exposes version {1} and timestamp {2}")
    @MethodSource("versionBundles")
    void gettersWorkAsExpected(VersionBundle versionBundle, String version, long timestamp) {
        assertEquals(version, versionBundle.getVersion());
        assertEquals(timestamp, versionBundle.getTimestamp());
    }

    private static Stream<Arguments> versionBundles() {
        return Stream.of(
                Arguments.of(new VersionBundle("foo"), "foo", -1L),
                Arguments.of(new VersionBundle("foo", -1), "foo", -1L),
                Arguments.of(new VersionBundle("foo", -17), "foo", -1L),
                Arguments.of(new VersionBundle("bar", 17), "bar", 17L)
        );
    }

    @ParameterizedTest(name = "{0}.toString() renders {1}")
    @MethodSource("stringRepresentations")
    void toStringWorksAsExpected(VersionBundle versionBundle, String expectedResult) {
        assertEquals(expectedResult, versionBundle.toString());
    }

    private static Stream<Arguments> stringRepresentations() {
        return Stream.of(
                Arguments.of(new VersionBundle("foo"), "foo#-1"),
                Arguments.of(new VersionBundle("foo", -1), "foo#-1"),
                Arguments.of(new VersionBundle("foo", -17), "foo#-1"),
                Arguments.of(new VersionBundle("bar", 17), "bar#17")
        );
    }

    @ParameterizedTest(name = "VersionBundle.fromString({0}) returns {1}")
    @MethodSource("fromStringArguments")
    void fromStringReturnsExpectedResult(String input, VersionBundle expectedResult) {
        VersionBundle actual = VersionBundle.fromString(input);
        if (expectedResult == null || actual == null) {
            assertEquals(expectedResult, actual);
        } else {
            assertEquals(0, expectedResult.compareTo(actual));
        }
    }

    private static Stream<Arguments> fromStringArguments() {
        return Stream.of(
                Arguments.of("foo", new VersionBundle("foo")),
                Arguments.of(" foo ", new VersionBundle("foo")),
                Arguments.of(" foo#17", new VersionBundle("foo", 17)),
                Arguments.of(" foo#17 ", new VersionBundle("foo", 17)),
                Arguments.of("foo#-1", new VersionBundle("foo", -1)),
                Arguments.of("foo#-17", new VersionBundle("foo", -1)),
                Arguments.of("foo#bar", new VersionBundle("foo", -1)),
                Arguments.of(null, null)
        );
    }

    @Test
    void compareToIsTimestampOnly() {
        VersionBundle instance1 = new VersionBundle("foo", -1);
        VersionBundle instance2 = new VersionBundle("foo", 10);
        VersionBundle instance3 = new VersionBundle("foo", 17);
        VersionBundle instance4 = new VersionBundle("bar", 17);

        assertEquals(0, instance1.compareTo(instance1));
        assertTrue(instance1.compareTo(instance2) < 0);
        assertTrue(instance1.compareTo(instance3) < 0);
        assertTrue(instance1.compareTo(instance4) < 0);

        assertTrue(instance2.compareTo(instance1) > 0);
        assertEquals(0, instance2.compareTo(instance2));
        assertTrue(instance2.compareTo(instance3) < 0);
        assertTrue(instance2.compareTo(instance4) < 0);

        assertTrue(instance3.compareTo(instance1) > 0);
        assertTrue(instance3.compareTo(instance2) > 0);
        assertEquals(0, instance3.compareTo(instance3));
        assertEquals(0, instance3.compareTo(instance4));

        assertTrue(instance4.compareTo(instance1) > 0);
        assertTrue(instance4.compareTo(instance2) > 0);
        assertEquals(0, instance4.compareTo(instance3));
        assertEquals(0, instance4.compareTo(instance4));

        assertTrue(instance1.compareTo(null) > 0);
    }

    @Test
    void constructorWithNullVersionThrows() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new VersionBundle(null));
        assertEquals("version must not be null!", exception.getMessage());
    }

    @Test
    void constructorWithNullVersionAndTimestampThrows() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new VersionBundle(null, 17));
        assertEquals("version must not be null!", exception.getMessage());
    }

    @Test
    void equalsAndHashCodeBehaveAsExpected() {
        VersionBundle instance1 = new VersionBundle("foo", 10);
        VersionBundle instance2 = new VersionBundle("foo", 10);
        VersionBundle instance3 = new VersionBundle("foo", 17);
        VersionBundle instance4 = new VersionBundle("bar", 17);
        VersionBundle instance5 = new VersionBundle("bar", 10);

        assertNotEquals(null, instance1);
        assertNotEquals(instance1, 1);
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
        assertNotEquals(instance1, instance4);
        assertNotEquals(instance1, instance5);

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
}
