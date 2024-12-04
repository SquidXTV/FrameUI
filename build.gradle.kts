import org.jreleaser.model.Active
import org.jreleaser.model.Distribution.DistributionType
import java.util.Calendar

plugins {
    id("java")
    id("com.diffplug.spotless") version "7.0.0.BETA4"
    `maven-publish`
    id("org.jreleaser") version "1.15.0"
}

group = "me.squidxtv"
version = "1.0.0"
description = "FrameUI is a Minecraft plugin library designed to easily create screens within a server."

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }
}

val spigotVersion = "1.21.4-R0.1-SNAPSHOT"
val packetEventsVersion = "2.6.0"
val junitVersion = "5.11.3"
val mockitoVersion = "5.14.2"

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
    compileOnly("com.github.retrooper:packetevents-spigot:$packetEventsVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
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
        if (file("$rootDir/server/plugins").exists()) {
            destinationDirectory.set(file("$rootDir/server/plugins"))
        }
        exclude("javadoc-overview.html")
    }

    javadoc {
        (options as StandardJavadocDocletOptions)
            .tags("apiNote:a:API Note:", "implNote:a:Implementation Note:")
            .links("https://hub.spigotmc.org/javadocs/spigot/", "https://javadocs.packetevents.com/")
            .overview(file("$rootDir/src/main/resources/javadoc-overview.html").toString())
            .destinationDirectory(destinationDir?.resolve("${rootProject.version}"))
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

spotless {
    encoding("UTF-8")
    java {
        endWithNewline()
        removeUnusedImports()

        importOrder("", "me.squidxtv", "java", "\\#")
        eclipse().configFile("${rootProject.rootDir}/config/custom-eclipse-style.xml")

        licenseHeader("""
            /*
             * FrameUI: Minecraft plugin library designed to easily create screens within a server.
             * Copyright (C) 2023-${'$'}YEAR Connor Schweighöfer
             *
             * This program is free software: you can redistribute it and/or modify
             * it under the terms of the GNU General Public License as published by
             * the Free Software Foundation, either version 3 of the License, or
             * (at your option) any later version.
             *
             * This program is distributed in the hope that it will be useful,
             * but WITHOUT ANY WARRANTY; without even the implied warranty of
             * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
             * GNU General Public License for more details.
             *
             * You should have received a copy of the GNU General Public License
             * along with this program. If not, see <https://www.gnu.org/licenses/>.
             */
        """.trimIndent())
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            group = rootProject.group
            artifactId = rootProject.name
            version = rootProject.version.toString()

            from(components["java"])

            pom {
                name = rootProject.name
                description = rootProject.description
                url = "https://github.com/SquidXTV/FrameUI"

                licenses {
                    license {
                        name = "GPL-3.0-or-later"
                        url = "https://spdx.org/licenses/GPL-3.0-or-later.html"
                    }
                }

                developers {
                    developer {
                        id = "squidxtv"
                        name = "Connor Schweighöfer"
                        email = "squidxtv@gmail.com"
                        url = "https://squidxtv.me/"
                        timezone = "Europe/Berlin"
                    }
                }

                scm {
                    connection = "scm:git:https://github.com/SquidXTV/FrameUI.git"
                    developerConnection = "scm:git:ssh://github.com/SquidXTV/FrameUI.git"
                    url = "https://github.com/SquidXTV/FrameUI.git"
                }
            }
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

jreleaser {
    project {
        name = rootProject.name
        version = rootProject.version.toString()
        description = rootProject.description

        author("Connor Schweighöfer")
        maintainer("SquidXTV")

        tag("java")
        tag("minecraft")
        tag("library")
        tag("spigot")

        license = "GPL-3.0-or-later"

        inceptionYear = "2023"
        copyright = "${inceptionYear}-${Calendar.getInstance().get(Calendar.YEAR)} Connor Schweighöfer"

        links {
            homepage = "https://github.com/SquidXTV/FrameUI"
            documentation = "https://squidxtv.me/FrameUI/${rootProject.version}/"
            faq = "https://github.com/SquidXTV/FrameUI/discussions"
            help = "https://github.com/SquidXTV/FrameUI/discussions"
            license = "https://spdx.org/licenses/GPL-3.0-or-later.html"
        }
    }

    signing {
        active = Active.ALWAYS
        armored = true
    }

    distributions {
        val stagingRepository = "build/staging-deploy/me/squidxtv/FrameUI/${rootProject.version}"

        create("Plugin") {
            active = Active.ALWAYS
            distributionType = DistributionType.SINGLE_JAR

            artifact {
                setPath("${stagingRepository}/FrameUI-${rootProject.version}.jar")
            }
        }

        create("Javadoc") {
            active = Active.ALWAYS
            distributionType = DistributionType.SINGLE_JAR

            artifact {
                setPath("${stagingRepository}/FrameUI-${rootProject.version}-javadoc.jar")
            }
        }

        create("Sources") {
            active = Active.ALWAYS
            distributionType = DistributionType.SINGLE_JAR

            artifact {
                setPath("${stagingRepository}/FrameUI-${rootProject.version}-sources.jar")
            }
        }
    }

    release {
        github {
            username = "SquidXTV"
            repoOwner = "SquidXTV"
            name = "FrameUI"
            host = "github.com"
        }
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}
