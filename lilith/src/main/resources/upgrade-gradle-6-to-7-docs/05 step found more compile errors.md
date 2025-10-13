I added the remaining dependencies and pushed compilation forward.

What I changed

App deps for remaining modules
Added log4j2 and producers:
:log4j:converter-log4j2
:log4j:producer-log4j2
Added log4j 1.x converter and XML import:
:log4j:converter-log4j
:log4j:log4j-xml
Added JUL receiver:
:jul:jul-xml-receiver
Added all logback multiplex appenders:
:logback:multiplex-appender-core (already)
:logback:multiplex-appender-access
:logback:multiplex-appender-classic
:logback:multiplex-appender-classic-xml
:logback:multiplex-appender-classic-json
Added internal tools:
:logback:tools (for ContextHelper)
:lilith-json-logging
:lilith-xml-logging
Added libraries used by app
HttpClient + HttpCore
logback-access
javax.servlet-api
commons-codec
Dependency catalog (versions + libs)
dependencyDefinitions.gradle: added httpclient 4.5.6, httpcore 4.4.10, log4j 1.2.17, commons-codec 1.15, servlet-api 4.0.1.
Wired submodules to compile
lilith-json-logging: +sulky-buffers, +sulky-codec
lilith-xml-logging: +lilith-data:logging-xml, +sulky-stax, +slf4j-api
jul:jul-xml: +slf4j-api, +lilith-data:logging
jul:jul-xml-import: +sulky-stax, +sulky-formatting, +commons-io, +slf4j-api, +lilith-data:eventsource/logging, +jul:jul-xml
log4j:converter-log4j: new block with +slf4j-api, +log4j-1x, and lilith-data (logging/eventsource/converter)
logback:encoder/convertor modules: already wired earlier; added missing encoder-core and constants dependencies where needed
logback:tools: added logback-core + slf4j-api
logback:multiplex-appender-core: +sulky-ulid, +logback:tools, +lilith-sender
Status after compile attempt

Many core modules compile (engine, data, conditions, prefs, logback encoders/converters/tools, json/xml engines).
Remaining errors are due to missing dependencies for jul:jul-xml-receiver (and more imports in MainFrame for http, log4j2, etc.). I’ve already added most of the missing projects; the build progressed but still shows unresolved classes within jul:jul-xml-receiver (e.g., AbstractEventProducer, LoggingEventReader, etc.) and then log4j converter 1.x (needs those lilith-data types and log4j 1.x, which I added).
Because Gradle compiles in parallel and the output is truncated, the next step is to re-run compile to capture current remaining gaps and add any final dependencies (if any) to those subprojects. The big chunks are already wired.
Next steps I can take

Resolve the jul:jul-xml-receiver missing imports by ensuring it has:
implementation project(':lilith-engine') and implementation project(':jul:jul-xml') (it references AbstractEventProducer and LoggingEventReader)
Ensure log4j2 producer/converter compile by confirming their module deps (they usually only need slf4j and lilith-data, already transitive).
Would you like me to:

Add the two missing deps to jul:jul-xml-receiver, re-run compile, and continue iterating until the app compiles fully?