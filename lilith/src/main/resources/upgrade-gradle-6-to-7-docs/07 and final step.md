Added the missing module wiring so every Lilith and decoder test can see the classes they expect.

### Key Updates
- projectDependencies.gradle:5-13 – let every Java subproject pull in slf4j-api for tests.
- projectDependencies.gradle:45-119 – expanded the Lilith app dependencies to include the Groovy tooling, Thymeleaf, log4j/logback helpers, corpus/test fixtures, and runtime stacks (woodstox, spring-webmvc, log4j2-core, etc.), plus Spock runtime bits (objenesis, byte-buddy).
- projectDependencies.gradle:137-207 – restored module-specific wiring: JSON/XML logging serializers now depend on :lilith-data:logging-io-test and woodstox, the Groovy corpus module depends on the lilith-data projects, and :jul/log4j XML modules carry woodstox at runtime.
- projectDependencies.gradle:246-420 – ensured all protobuf modules, lilith-engine, and log4j:decoder-log4j2 bring in their test fixtures and Jackson/YAML/XML parsers; added Spock runtime libs where needed.
- dependencyDefinitions.gradle:3-80 – reintroduced version coordinates for byte-buddy, spring, woodstox, and the Jackson/Spring helper artifacts used above.
- lilith/src/main/java/de/huxhorn/lilith/Lilith.java:546-715 – removed the unused logger in startLilith and suppressed PMD on the dormant JUnique callback.

### Build Verification
- JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu11.74.15-ca-11-0-24.jdk/zulu-11.jdk/Contents/Home ./gradlew clean
- JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu11.74.15-ca-11-0-24.jdk/zulu-11.jdk/Contents/Home ./gradlew build
- Both commands now finish successfully (Gradle still emits the existing illegal-reflective-access warnings from Groovy). All modules compile, and the full test suite runs without the earlier Spock or log4j parser failures.