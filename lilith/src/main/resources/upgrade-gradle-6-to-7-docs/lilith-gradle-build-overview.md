# Lilith Gradle build overview

Scope
- Multi-module Gradle build for a Swing desktop app and libraries
- Gradle wrapper: 7.6
- Java compatibility: source/target 1.8
- Packaging: library modules (Maven publications) + application module with shaded and classic distributions

Versioning and release flags
- Base version: 8.5.0
- Default: SNAPSHOT; use `-Drelease` for release or `-Dprerelease=<tag>` for prerelease (e.g., `alpha.1`)
- PGP signing required for non-SNAPSHOT builds: supply `-DpgpPassword` and GPG secret keyring at `~/.gnupg/secring.gpg`

Repositories
- Local Maven directories
  - Releases: `~/local-gradle-repository/release`
  - Snapshots: `~/local-gradle-repository/snapshot`
- Remote: Maven Central and mavenLocal

Default tasks
- Without `-Drelease`/`-Dprerelease`:
  - `build`, `uploadPublished`, `shadowJar`, `myDistZip`, `myDistTgz`
- With `-Drelease` or `-Dprerelease`:
  - Above plus `signJavadocZip`, `signSourceZip`

Quality, coverage, documentation
- Checkstyle (config/checkstyle), PMD (config/pmd), Forbidden APIs (fail on violations by default)
- JaCoCo per-module + aggregated `jacocoRootReport` (HTML/XML); Coveralls supported
- `javadocAll` across subprojects; `sourceZip` and `javadocZip`; doclint disabled for protobuf modules

Publishing (all subprojects)
- `maven-publish` publication "maven" from `components.java`
- Artifacts: `jar`, `-sources.jar`, `-javadoc.jar`
- Repos: LocalRelease (releasesOnly), LocalSnapshot (snapshotsOnly)
- `uploadPublished` routes to the correct repo based on version suffix
- Signing applies to publications for non-SNAPSHOT versions

Application module: `:lilith`
- Main class: `de.huxhorn.sulky.version.Main`
- Application plugin; `jar` manifest sets `Main-Class` + runtime `Class-Path`
- Shadow plugin
  - `shadowJar` is signed on non-SNAPSHOT
  - `shadowDistZip`/`shadowDistTar` configured
- Custom distributions
  - `myDistZip`/`myDistTgz`: folder `lilith-${version}` containing
    - Licensing/readme files
    - `bin/lilith` (POSIX filtered) and `bin/lilith.bat`
    - `lib` with application jars; renames main jar to `lilith.jar`; excludes sources/javadoc jars

Aggregated coverage
- `jacocoRootReport` collects execution data from all subprojects and writes XML to `build/reports/jacoco/report.xml`
- Coveralls reads the aggregated XML report

Local repo maintenance and sync
- `cleanStaging` and `cleanSnapshot` wipe local release/snapshot repos
- `syncStaging` and `syncSnapshot` push local repos to Sonatype via WebDAV (needs `-PremoteUsername`/`-PremotePassword`)

Module layout
- Modules included via `settings.gradle` by scanning `lilith-data`, `jul`, `log4j`, `logback` directories plus top-level modules
- Versions and coordinates centralized in `dependencyDefinitions.gradle`
- Per-module dependencies and metadata in `projectDependencies.gradle`

Typical commands
- Fast local build: `./gradlew build`
- Assemble app artifacts: `./gradlew :lilith:shadowJar :lilith:myDistZip :lilith:myDistTgz`
- Publish libraries locally: `./gradlew uploadPublished`
- Aggregated coverage + Coveralls: `./gradlew test jacocoRootReport coveralls`
- Release build (signed): `./gradlew -Drelease -DpgpPassword=*** build uploadPublished shadowJar myDistZip myDistTgz signJavadocZip signSourceZip`
