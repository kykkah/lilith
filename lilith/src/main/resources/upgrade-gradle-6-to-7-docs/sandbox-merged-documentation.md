# Lilith sandbox modules: consolidated documentation

Purpose
- Small, runnable apps to manually test and demonstrate Lilith integrations across logging stacks (Logback, Log4j 1.x/2, JUL) and transports (e.g., sockets, rolling files, JGroups).
- Validate encoders/appenders, layouts (XML/JSON/YAML), MDC/markers, and exception trees, independent from the main modules.

Common wiring
- Each sandbox is an `application` with a `mainClass` and minimal dependencies.
- `sandbox/sandboxDefaults.gradle` defines repositories and `lilithVersion` (e.g., `8.4.0-SNAPSHOT`) to resolve Lilith artifacts from local Maven:
  - `~/local-gradle-repository/release`
  - `~/local-gradle-repository/snapshot`
- Typical workflow:
  1) Publish Lilith modules locally: `./gradlew uploadPublished` (from repo root).
  2) Run a sandbox against the freshly published artifacts.

Submodules overview
- logback-classic-sandbox
  - Main: `de.huxhorn.lilith.sandbox.LogbackClassicSandbox`
  - Exercises MDC, markers, exception chains (causes/suppressed), and multiple appenders.
  - Config: `src/main/resources/logback.xml` includes `ClassicLilithEncoder` and `ClassicMultiplexSocketAppender`; writes to files and sockets.

- logback-access-sandbox
  - Spring Boot sample for Logback Access HTTP logging with Lilith components.
  - Run with `./gradlew bootRun` inside the module.

- log4j-sandbox (Log4j 1.x)
  - Main: `de.huxhorn.lilith.sandbox.Log4jSandbox`
  - Generates varied events and exception shapes to validate encoders/decoders and XML output.

- log4j2-sandbox (Log4j 2.x)
  - Main: `de.huxhorn.lilith.sandbox.Log4j2Sandbox`
  - Uses Log4j2 API/Core with Jackson at runtime for XML/JSON/YAML layouts; tests producers/decoders and conversions.

- jul-sandbox (java.util.logging)
  - Main: `de.huxhorn.lilith.sandbox.JulSandbox`
  - Used with JUL XML tools/importers to validate parsing and conversion flows.

- jgroups-sandbox
  - Main: `de.huxhorn.lilith.sandbox.JGroupsSandbox`
  - Experiments with clustered/multiplexed logging transport using JGroups alongside SLF4J/Logback.

How to run
- Publish artifacts locally (once per change):
  - `./gradlew uploadPublished`
- Execute a sandbox:
  - Logback Classic: `./gradlew :sandbox:logback-classic-sandbox:run`
  - Log4j 1.x: `./gradlew :sandbox:log4j-sandbox:run`
  - Log4j 2.x: `./gradlew :sandbox:log4j2-sandbox:run`
  - JUL: `./gradlew :sandbox:jul-sandbox:run`
  - JGroups: `./gradlew :sandbox:jgroups-sandbox:run`
  - Logback Access: `cd sandbox/logback-access-sandbox && ./gradlew bootRun`

Notes
- If dependencies don’t resolve, ensure `sandboxDefaults.gradle:lilithVersion` matches what you published locally.
- You can freely tweak sandbox configs (e.g., `logback.xml`) to reproduce issues without affecting main modules.
