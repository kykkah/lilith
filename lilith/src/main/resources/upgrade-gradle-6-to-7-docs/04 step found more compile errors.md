Made the requested changes and pushed the build forward. Here’s what I fixed and where it stands now.

What I changed

Commented JUnique
lilith/projectDependencies.gradle:61 — disabled the app’s JUnique dependency.
lilith/dependencyDefinitions.gradle:48 — commented out JUnique coordinate.
Added app logback module deps
lilith/projectDependencies.gradle: app now depends on:
logback:encoder-core, encoder-classic, encoder-access, converter-classic, converter-access, multiplex-appender-core
Fixed AccessFormatter compile inputs
Added logback-access and javax.servlet-api to the app.
Added logback:tools to the app (for ContextHelper).
AccessFormatter compiles with servlet + logback tools on the classpath.
Wired missing inter-module deps so subprojects compile

JSON/XML engine modules
lilith-json-logging now depends on lilith-engine, lilith-data:logging/eventsource/logging-json-serializer, sulky-buffers, sulky-codec.
lilith-xml-logging now depends on lilith-engine, lilith-data:logging/eventsource/logging-xml/logging-xml-serializer, slf4j-api, sulky-buffers, sulky-codec, sulky-stax.
JUL XML import
jul:jul-xml and jul:jul-xml-import now depend on lilith-data:logging/eventsource and sulky-stax/formatting, plus slf4j and commons-io.
Logback encoders/converters
encoder-core: + lilith-constants.
encoder-access/classic: + encoder-core + converter-access/classic + lilith-constants + lilith-data (access/logging/logging-protobuf/access-protobuf) + converter where needed.
converter-access/classic: + lilith-data (access/logging/eventsource/converter) + logback-access/classic.
multiplex-appender-core: + sulky-ulid, logback:tools, lilith-sender.
logback:tools: added in projectDependencies with logback-core + slf4j-api.
Log4j converter (1.x)
Added log4j:converter-log4j block and dependencies on lilith-data (logging/eventsource/converter), slf4j, and log4j 1.x.
App module extras
Added commons-codec (for CreateMd5Command), commons-lang3/text, flying-saucer-core, glazedlists, logback:tools.
Added lilith-json-logging, lilith-xml-logging, jul:jul-xml-import, and log4j:converter-log4j.
Build status with your JDK 11

Good
Sulky built/published locally (source/target bumped to 1.8).
Most lilith subprojects now compile (engine, conditions, data modules, logback tools/encoders/converters, json/xml logging producers after wiring).
AccessFormatter-related errors are addressed by adding servlet and tools dependencies.
Remaining compile errors
App still fails due to unresolved imports used in MainFrame and other app classes that pull in more modules:
Missing imports for log4j2 converter/producer packages and logback appender packages.
Missing Apache HttpClient types used by MainFrame.
Also, jul/log4j/lilith app wiring still needs a few more dependencies (log4j2 packages and HTTP client).
Proposed next fixes

Add dependencies for the remaining app imports:
log4j2 producer/converter modules:
implementation project(':log4j:converter-log4j2')
implementation project(':log4j:producer-log4j2')
logback appender modules:
implementation project(':logback:multiplex-appender-classic')
implementation project(':logback:multiplex-appender-classic-xml')
implementation project(':logback:multiplex-appender-classic-json')
implementation project(':logback:multiplex-appender-access')
HTTP client:
Add Apache HttpComponents (httpclient + httpcore) coordinates to dependencyDefinitions, then to app (MainFrame uses org.apache.http.*).
After adding those, rerun ./gradlew :lilith:compileJava -x test and iterate on any remaining small gaps.
Want me to add the above app dependencies and retry compile now?