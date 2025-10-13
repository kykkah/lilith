Applied your requested changes and made a compile attempt.

What I changed

Commented all junique references

lilith/projectDependencies.gradle:61
Commented the app dependency:
// implementation libraries.'junique'
lilith/dependencyDefinitions.gradle:48
Commented the library mapping:
// 'junique' : 'de.huxhorn.lilith:de.huxhorn.lilith.3rdparty.junique:1.0.4'
left code comments in Lilith.java untouched (they were already commented)
Added app logback module deps

App depends on internal logback modules required to compile:
lilith/projectDependencies.gradle:33,52,94,106,118,129
Under project(':lilith') dependencies added:
implementation project(':logback:encoder-core')
implementation project(':logback:encoder-classic')
implementation project(':logback:encoder-access')
implementation project(':logback:converter-classic')
implementation project(':logback:converter-access')
implementation project(':logback:multiplex-appender-core')
Wired transitive deps so logback submodules compile

Added sulky, lilith-data, logback external dependencies to the relevant logback modules:
converter-classic → logback-classic, logback:classic, lilith-data:logging, lilith-data:converter, lilith-data:eventsource
converter-access → logback-access, lilith-data:access, lilith-data:converter, lilith-data:eventsource
encoder-core → logback-core, lilith-constants
encoder-classic → logback-classic, logback:converter-classic, logback:encoder-core, lilith-constants, lilith-data:logging, lilith-data:eventsource, lilith-data:logging-protobuf, lilith-data:converter
encoder-access → logback-access, logback:converter-access, logback:encoder-core, lilith-constants, lilith-data:access, lilith-data:eventsource, lilith-data:access-protobuf, lilith-data:converter
multiplex-appender-core → logback-core, sulky-ulid, logback:tools, lilith-sender
Added a new block for logback:tools with logback-core and slf4j-api
Added a few missing externals used by app UI and JSON

dependencyDefinitions.gradle: added Jackson, commons-lang3, commons-text, GlazedLists (using com.glazedlists:glazedlists:1.11.0)
project(':lilith') now includes commons-lang3, commons-text, flying-saucer-core, glazedlists
Jackson + wiring for lilith-data:logging-json-serializer
Compile attempt (with your JDK 11)

Command: ./gradlew --no-daemon :lilith:compileJava -x test
Progress: All lilith-data and logback submodules now compile, and encoder-core/encoder-access/encoder-classic compiled after wiring.
Current blocker: app module (:lilith) fails compiling within AccessFormatter inner classes due to missing base constants and overrides:
Errors: “method does not override or implement a method from a supertype” and “cannot find symbol NA / SENTINEL” in lilith/lilith/src/main/java/de/huxhorn/lilith/tools/formatters/AccessFormatter.java lines around 395–496.
These are internal code issues (not dependency-related) and need fixing in AccessFormatter (likely missing imports or wrong interface signature/fields).
Summary

JUnique is commented out in Gradle.
App logback module deps added and wired; most subprojects compile under JDK 11.
Build is now blocked by code-level errors in AccessFormatter, not dependency wiring.
Do you want me to fix AccessFormatter next (adjust method signatures/fields, ensure it matches the formatter interfaces), or stop here?