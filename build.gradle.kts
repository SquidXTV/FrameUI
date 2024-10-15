plugins {
    id("java")
    `maven-publish`
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
    withSourcesJar()
    withJavadocJar()
}

tasks {
    jar {
        destinationDirectory.set(file("$rootDir/server/plugins"))
        exclude("javadoc-overview.html")
    }

    javadoc {
        (options as StandardJavadocDocletOptions)
            .tags("apiNote:a:API Note:", "implNote:a:Implementation Note:")
            .links("https://hub.spigotmc.org/javadocs/spigot/", "https://javadocs.packetevents.com/")
            .overview(file("$rootDir/src/main/resources/javadoc-overview.html").toString())
    }

    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            group = "me.squidxtv"
            artifactId = "FrameUI"
            version = version

            from(components["java"])
        }
    }
}
