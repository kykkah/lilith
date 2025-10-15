/*
 * Lilith - a log event viewer.
 */

package de.huxhorn.lilith;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeFormattersTest {

    private static TimeZone defaultTimeZone;

    @BeforeAll
    static void setUpTimeZone() {
        defaultTimeZone = TimeZone.getDefault();
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        TimeZone.setDefault(timeZone);
    }

    @AfterAll
    static void resetTimeZone() {
        TimeZone.setDefault(defaultTimeZone);
    }

    @ParameterizedTest(name = "DATETIME_IN_SYSTEM_ZONE_SPACE formats {0} -> {1}")
    @MethodSource("dateTimeInSystemZoneSpaceArguments")
    void dateTimeInSystemZoneSpace(long millis, String expectedResult) {
        String result = DateTimeFormatters.DATETIME_IN_SYSTEM_ZONE_SPACE.format(Instant.ofEpochMilli(millis));
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> dateTimeInSystemZoneSpaceArguments() {
        return Stream.of(
                Arguments.of(0L, "1970-01-01 01:00:00.000"),
                Arguments.of(1_449_658_372_097L, "2015-12-09 11:52:52.097")
        );
    }

    @ParameterizedTest(name = "TIME_IN_SYSTEM_ZONE formats {0} -> {1}")
    @MethodSource("timeInSystemZoneArguments")
    void timeInSystemZone(long millis, String expectedResult) {
        String result = DateTimeFormatters.TIME_IN_SYSTEM_ZONE.format(Instant.ofEpochMilli(millis));
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> timeInSystemZoneArguments() {
        return Stream.of(
                Arguments.of(0L, "01:00:00.000"),
                Arguments.of(1_449_658_372_097L, "11:52:52.097")
        );
    }

    @ParameterizedTest(name = "COMPACT_DATETIME_IN_SYSTEM_ZONE_T formats {0} -> {1}")
    @MethodSource("compactDateTimeArguments")
    void compactDateTimeInSystemZone(long millis, String expectedResult) {
        String result = DateTimeFormatters.COMPACT_DATETIME_IN_SYSTEM_ZONE_T.format(Instant.ofEpochMilli(millis));
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> compactDateTimeArguments() {
        return Stream.of(
                Arguments.of(0L, "19700101T010000000"),
                Arguments.of(1_449_658_372_097L, "20151209T115252097")
        );
    }
}
