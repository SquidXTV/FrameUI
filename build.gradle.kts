plugins {
    id("java")
}

group = "me.squidxtv"
version = "1.0-SNAPSHOT"
description = "FrameUI"

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }
}

val spigotVersion = "1.21.1-R0.1-SNAPSHOT"
val packetEventsVersion = "2.5.0"
val mockitoVersion = "5.14.2"

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
    compileOnly("com.github.retrooper:packetevents-spigot:$packetEventsVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter.junit-jupiter:5.11.2")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.spigotmc:spigot-api:$spigotVersion")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}
