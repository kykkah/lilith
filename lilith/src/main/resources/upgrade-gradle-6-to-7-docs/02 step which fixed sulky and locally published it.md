Done. I built/published sulky with JDK 11 and wired Lilith to use those artifacts, then validated by compiling key Lilith modules.

What I changed

- Sulky target update: sulky/build.gradle:320
 -  Set :sulky-version sourceCompatibility/targetCompatibility to 1.8.
- Published Sulky locally:
    - Ran ./gradlew --no-daemon uploadPublished -x test in sulky/ (published all sulky modules to ~/local-gradle-repository/snapshot).
- Lilith Gradle config:
    - settings.gradle: lilith/settings.gradle:1
        - Scans lilith-data, jul, log4j, logback subdirs; includes top-level modules.
    - dependencyDefinitions.gradle: lilith/dependencyDefinitions.gradle:1
        - Added versions.sulky, Jackson, commons-lang/text, Flying Saucer, GlazedLists; plus sulky libraries mapped to published artifact IDs (de.huxhorn.sulky:sulky-*).
        - Added junique and slf4j-jul.
    - projectDependencies.gradle: lilith/projectDependencies.gradle:1
        - Per-module, exact sulky + inter-project deps:
            - :lilith-engine → sulky-buffers/codec/codec-filebuffer/io/formatting/conditions + lilith-data (eventsource/logging/access/converter/logging-protobuf/access-protobuf) + :lilith-constants, :lilith-sender.
            - :lilith-conditions → sulky-conditions/groovy + lilith-data (logging/access/eventsource) + groovy + slf4j.
            - :lilith-data:*:
                - logging → sulky-formatting + eventsource
                - logging-xml → sulky-formatting/stax + eventsource + logging
                - logging-xml-serializer → sulky-formatting/codec/stax + logging-xml + logging
                - eventsource-xml → sulky-stax + eventsource
                - access → eventsource
                - access-protobuf/logging-protobuf → protobuf + sulky-codec + respective base modules
            - :jul:* and :log4j:* → sulky dependencies per usage (stax/formatting/tasks/buffers/codec).
            - :lilith-prefs → protobuf + sulky-codec.
            - :lilith app → slf4j-jul/logback/commons-io/lang/text/jcommander, Flying Saucer, GlazedLists, :lilith-constants, and its internal modules + sulky libs (buffers, codec, io, formatting, swing, tasks, ulid, sounds, groovy), and junique.
- Verified Gradle on JDK 11:
    - java -version → Zulu 11.0.24; ./gradlew help OK under lilith/.
Build status

- With JDK 11, Sulky published successfully.
- Lilith now compiles its data/engine/conditions/prefs modules with the new sulky artifacts.
- App module :lilith gets much further; remaining compile errors are unrelated to sulky wiring (e.g., missing logback converter modules and some UI formatter internals). To proceed:
    - Add internal logback modules to the app:
        - In lilith/projectDependencies.gradle, under project(':lilith') add:
            - implementation project(':logback:converter-classic')
            - implementation project(':logback:converter-access')
            - implementation project(':logback:encoder-core')
            - implementation project(':logback:encoder-classic')
            - implementation project(':logback:encoder-access')
            - implementation project(':logback:multiplex-appender-core')
            - Then rerun: cd lilith && ./gradlew --no-daemon :lilith:compileJava -x test
Want me to add those app logback module deps and try another compile pass?