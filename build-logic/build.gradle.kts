import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    `kotlin-dsl`
    groovy
}

group = "de.huxhorn.lilith.buildlogic"
version = "1.0-SNAPSHOT"

tasks.wrapper {
    gradleVersion = "8.13"
    distributionType = Wrapper.DistributionType.ALL
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("com.github.johnrengelman.shadow:com.github.johnrengelman.shadow.gradle.plugin:8.1.1")
    implementation("com.github.johnrengelman:shadow:8.1.1")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apache.ant:ant:1.10.13")
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.ow2.asm:asm-commons:9.4")
    implementation("de.thetaphi:forbiddenapis:3.10")
    implementation(localGroovy())
}
