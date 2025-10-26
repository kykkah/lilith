### Make sure this settings.gradle exists:
<pre>
// This plugin is only applied in the settings.gradle file, as it needs to be applied before any other build logic is executed.
// This plugin ensures Gradle runs on top of the correct JDK version, either without a JDK installed or with a different JDK version than the one required by the project. Also, it ensures that all developers and CI systems run Gradle on top of the compatible JDK version. Remember that Gradle wrapper only ensures the correct Gradle version, not the correct JDK version.
// This plugin creates or updates the gradle/gradle-daemon-jvm.properties file via the command "./gradlew updateDaemonJvm". It requires Gradle 7.6 or higher.
// Its configuration can be found in the file gradle/toolchains/foojay-resolver-convention.gradle.gradle
// See https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention for more details.
plugins {
    id 'org.gradle.toolchains.foojay-resolver-convention' version '0.9.0'
}
</pre>

### Invoke this command to upgrade gradle version in the wrapper:
- cd sandbox/jul-sandbox && ./gradlew wrapper --gradle-version=8.13 && ./gradlew wrapper

### Then Tie JDK17 to the daemon via wiring the Foojay toolchain resolution so the built-in updateDaemonJvm task can provision JDK 17 when you run it.
- “./gradlew help --task updateDaemonJvm” to confirm the new “updateDaemonJvm” task is present; then run:
- ./gradlew updateDaemonJvm --jvm-version=17 --jvm-vendor=GRAAL_VM

### run or test
- ./gradlew run
- ./gradlew test