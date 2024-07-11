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

val spigotVersion = "1.20.6-R0.1-SNAPSHOT"
val packetEventsVersion = "2.4.0"
val mockitoVersion = "5.12.0"

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
    compileOnly("com.github.retrooper:packetevents-spigot:$packetEventsVersion")

    testImplementation("org.spigotmc:spigot-api:$spigotVersion")
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
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
