plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)
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
}
