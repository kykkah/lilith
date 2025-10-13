The Gradle wiring now includes the missing JUL receiver links and the rest of the app modules compile cleanly with the specified JDK.

### Changes

- Ensured jul:jul-xml-receiver depends on :lilith-engine and :jul:jul-xml, keeping its build block intact at projectDependencies.gradle:289-300.
- Restored and expanded the Lilith app’s dependency list to cover Groovy tooling, Thymeleaf, log4j/logback converters, and classic support so the UI classes resolve again (projectDependencies.gradle:35-110).
- Reintroduced per-module dependency blocks for the log4j converters/decoders, logback multiplex appenders, shutdown listener, and the logging IO test harness, wiring in the needed lilith-data and sulky artifacts (projectDependencies.gradle:320-540 and 147-156).
- Added the missing catalog entries for Groovy console/XML, Thymeleaf, and reset jcommander to 1.72 to match the existing API usage (dependencyDefinitions.gradle:5-62).
### Verification

JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu11.74.15-ca-11-0-24.jdk/zulu-11.jdk/Contents/Home ./gradlew compileJava
### Next steps (optional):

Run ./gradlew test to exercise the unit and integration suites.
If you need distribution artifacts, follow with ./gradlew build.