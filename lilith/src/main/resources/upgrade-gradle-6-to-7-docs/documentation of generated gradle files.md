Here’s a clean Gradle setup for lilith, created from the two docs.

What I added

- settings.gradle: lilith/settings.gradle:1
- build.gradle: lilith/build.gradle:1
- dependencyDefinitions.gradle: lilith/dependencyDefinitions.gradle:1
- projectDependencies.gradle: lilith/projectDependencies.gradle:1
### Highlights

- Module discovery: settings scans top-level lilith modules and submodules in lilith-data, jul, log4j, logback. Also includes 3rdparty:junique if present.
- Central versions: dependencyDefinitions.gradle defines a versions map and reusable libraries map (slf4j, logback, groovy, junit, protobuf, commons-io, jcommander).
- Streamlined build: build.gradle targets Gradle 7.6, Java 8, defaultTasks 'build', convenience tasks appPackage, publishLocal, coverageReport, and aggregated jacocoRootReport + Coveralls wiring.
- Repos + publishing: all projects use mavenLocal, Maven Central and the local release/snapshot dirs; maven-publish selects the local repo based on SNAPSHOT suffix; signing only for publications on non-SNAPSHOT.
- Quality: subprojects get checkstyle, PMD, and forbiddenApis configured to match the overview.
- App packaging: project ':lilith' uses application + shadow; main class set to de.huxhorn.lilith.Lilith; standard distZip/distTar + shadowJar.
### Usage

- Build: ./gradlew build
- Package app: ./gradlew appPackage
- Local publish: ./gradlew publishLocal
- Coverage: ./gradlew coverageReport
### Notes

- JDK compatibility: Gradle 7.6 doesn’t support very new JDKs. If you hit “Unsupported class file major version”, run with a JDK ≤ 19 (e.g., set JAVA_HOME or use a Gradle toolchain).
- Per-module deps: projectDependencies.gradle includes sensible defaults and the app wiring, but you’ll likely want to expand with specific dependencies per subproject as you iterate.
- Update the gradlew via "./gradlew wrapper --gradle-version=8.14.2 && ./gradlew wrapper"

Want me to add per-module dependency blocks for key modules next, or try a local gradle run with a compatible JDK?
