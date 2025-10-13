### Outcome

- Help/tasks succeeded.
- Compile failed in app module due to missing dependencies on internal lilith modules and sulky libraries.
    - Examples: de.huxhorn.lilith.data., de.huxhorn.lilith.engine., de.huxhorn.sulky.buffers., de.huxhorn.sulky.swing., etc. (see lilith/lilith/src/... errors)
- Root cause: projectDependencies.gradle currently only wires the app packaging and base test deps. It doesn’t yet declare inter-module deps or external sulky coordinates.
### Options to proceed

- Publish sulky and wire libs (recommended)
    - Build/publish sulky to local repo: run in sibling sulky/ → ./gradlew uploadPublished
    - Add sulky dependencies (e.g., slf4j/formatting/buffers/codec/io/tasks/swing/ulid/sounds) into lilith/projectDependencies.gradle per module.
- Composite build for local dev
    - Add includeBuild("../sulky") in lilith/settings.gradle and declare sulky dependencies by Maven coords; Gradle will substitute to the included projects. Note: sulky’s “sulky-version” subproject targets source 1.6, which may need JDK 8 or a compatibility tweak if we build sulky under JDK 11.
- Quick unblock
    - I can add a broad set of sulky implementation dependencies to all lilith subprojects (pragmatic for a first green build), then refine per module.
    - Also add inter-project dependencies for ':lilith' to lilith-engine, lilith-data (logging, access, converter, eventsource), and others it uses.
### Your call

- Want me to:
    1. Publish sulky locally and wire exact per-module dependencies, or
    2. Add a broad initial dependency set + includeBuild for fast compile, then iterate?
- If you prefer publishing sulky, do you have a JDK 8 path I should use for the sulky build (to satisfy its 1.6 target in :sulky-version), or should I bump that module’s target/source to 1.8 locally?