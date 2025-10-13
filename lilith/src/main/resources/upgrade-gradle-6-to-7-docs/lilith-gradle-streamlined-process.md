# Lilith Gradle build: streamlined process

Goals
- Remove redundant tasks and plugin overhead
- Standardize packaging and publishing
- Improve cacheability, parallelism, and CI speed
- Keep local dev flow simple; make release flow explicit

Key recommendations
1) Prefer a single distribution mechanism
- Keep `shadowJar` for a single runnable fat JAR
- Replace custom `myDistZip`/`myDistTgz` with standard `distZip`/`distTar` using the same CopySpec
- If both thin and fat distributions are required, keep only one pair (standard `distZip`/`distTar` or shadow counterparts), not both

2) Simplify default tasks and surface convenience tasks
- Default: `build`
- Add convenience aggregators:
  - `appPackage`: depends on `:lilith:shadowJar`, `:lilith:distZip`, `:lilith:distTar`
  - `publishLocal`: depends on `uploadPublished`
  - `coverageReport`: depends on `test`, `jacocoRootReport`

3) Scope signing to publications
- Sign Maven publications only; avoid signing `shadowJar` and distro archives unless mandated
- Continue signing `-sources.jar` and `-javadoc.jar` on non-SNAPSHOT builds

4) Modernize configuration
- Use `plugins {}` block where possible; pin plugin versions centrally
- Adopt Version Catalog (`gradle/libs.versions.toml`) for dependency versions
- Remove legacy `runtime` configuration shim unless macAppBundle is enabled
- Prefer lazy properties and avoid task graph hooks to unlock configuration cache

5) Keep quality/coverage practical
- Keep Checkstyle/PMD/Forbidden APIs; optionally set `ignoreFailures=true` locally; enforce in CI
- Generate JaCoCo XML only in CI to speed local builds; keep HTML as-needed

6) Publishing and repos
- Keep local release/snapshot repos and a single publish entry-point (`uploadPublished` or `publishLocal` task)
- Defer Sonatype sync to an explicit CI job or manual step; do not run by default

Suggested task layout
- Default: `build`
- Convenience:
  - `appPackage`: app binaries and archives
  - `publishLocal`: Maven publication to local repos
  - `coverageReport`: aggregated coverage report

Recommended commands
- Local dev:
  - Build fast: `./gradlew build`
  - Tests + coverage: `./gradlew coverageReport`
  - Package app: `./gradlew appPackage`
- Local publish:
  - `./gradlew publishLocal`
- CI (with caches):
  - PR: `./gradlew --no-daemon --parallel -Dorg.gradle.caching=true clean build`
  - Main: `./gradlew --no-daemon --parallel -Dorg.gradle.caching=true clean build coverageReport`
- Release:
  - Signed publications: `./gradlew -Drelease -DpgpPassword=*** clean build publishLocal`
  - Optional Sonatype sync via a separate credentialed job

Why this reduces redundancy
- One distribution path instead of parallel custom and shadow variants
- One publish entry point
- Signing only where necessary (publications)
- Explicit convenience tasks instead of overloading `defaultTasks`
- CI flags applied in pipeline, not embedded in build logic

Optional niceties
- Enable configuration cache: `org.gradle.configuration-cache=true`
- Enable build cache: `org.gradle.caching=true` (local/global)
- Use Java toolchains for consistent JDK provisioning
- Incremental static analysis in CI for changed modules only
