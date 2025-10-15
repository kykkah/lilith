Execute

```
JAVA_HOME=$(/usr/libexec/java_home -v 17) ./gradlew bootRun
```

Gradle 7.6 and this sample currently require running with a JDK 17 runtime so that
the Spring Boot tooling stays compatible.
