/*
 * Copyright (c) 2024 IBA Group.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBA Group
 */

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)
fun dateValue(pattern: String): String =
    LocalDate.now(ZoneId.of("Europe/Warsaw")).format(DateTimeFormatter.ofPattern(pattern))

plugins {
    antlr
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.2"
    id("org.jetbrains.changelog") version "2.2.1"
    java
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()
val antlrVersion = "4.13.2"
val junitJupiterVersion = "5.10.3"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    antlr("org.antlr:antlr4:$antlrVersion")
    implementation("org.antlr:antlr4-runtime:$antlrVersion")
    implementation("org.antlr:antlr4-intellij-adaptor:0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.1")

    // To have a dependency on zowe explorer from the marketplace
    // plugins.set(listOf("org.zowe.explorer:1.2.2-231"))

    // To have a dependency on built-in plugin from \Project_dir\libs\for-mainframe
    // plugins.set(listOf("${projectDir}\\libs\\for-mainframe"))
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
//changelog {
//    version.set(properties("pluginVersion").get())
//    header.set(provider { "${version.get()} (${dateValue("yyyy-MM-dd")})" }.get())
//    groups.set(listOf("Breaking changes", "Features", "Bugfixes", "Deprecations", "Security"))
//    keepUnreleasedSection.set(false)
//    itemPrefix.set("*")
//    repositoryUrl.set(properties("pluginRepositoryUrl").get())
//    sectionUrlBuilder.set { repositoryUrl, currentVersion, previousVersion, isUnreleased: Boolean ->
//        repositoryUrl + when {
//            isUnreleased -> when (previousVersion) {
//                null -> "/commits"
//                else -> "/compare/$previousVersion...HEAD"
//            }
//
//            previousVersion == null -> "/commits/$currentVersion"
//            else -> "/compare/$previousVersion...$currentVersion"
//        }
//    }
//}

idea {
    module {
        generatedSourceDirs.add(file("build/generated-src/antlr/main"))
    }
}

sourceSets {
    create("generated") {
        java.srcDirs("generated-src/antlr/main")
    }
    main {
        java.srcDirs("src/main", "build/generated-src/antlr/main")
        kotlin.srcDirs("src/main")
    }
    test {
        java.srcDirs("src/test")
        kotlin.srcDirs("src/test")
    }
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    patchPluginXml {
        version.set("${properties("pluginVersion").get()}-${properties("sinceBuildVersion").get().substringBefore(".")}")
        sinceBuild = properties("sinceBuildVersion")
        untilBuild = properties("untilBuildVersion")

//        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
//        changeNotes.set(
//            properties("pluginVersion")
//                .map { pluginVersion ->
//                    with(changelog) {
//                        renderItem(
//                            (getOrNull(pluginVersion) ?: getUnreleased())
//                                .withHeader(false)
//                                .withEmptySections(false),
//                            Changelog.OutputType.HTML,
//                        )
//                    }
//                }
//                .get()
//        )
    }

    test {
        // useJUnitPlatform()
        // see https://youtrack.jetbrains.com/issue/IDEA-278926
        isScanForTestClasses = false
        include("**/*Test*")
    }

    generateGrammarSource {
        maxHeapSize = "128m"
        arguments.addAll(listOf("-package", "eu.ibagroup.rexx", "-visitor", "-no-listener"))
        outputDirectory = file("build/generated-src/antlr/main")

        doLast {
            file("build/generated-src/antlr/main").walkTopDown().forEach { file ->
                if (file.isFile && file.extension == "java") {
                    val targetDir = file("build/generated-src/antlr/main/eu/ibagroup/rexx")
                    targetDir.mkdirs()

                    val targetFile = targetDir.resolve(file.name)
                    file.copyTo(targetFile, overwrite = true)
                    file.delete()
                }
            }
        }
    }

    compileJava {
        dependsOn(generateGrammarSource)

//        kotlinOptions {
//            jvmTarget = JavaVersion.VERSION_17.toString()
//        }
    }

    compileTestKotlin {
//        dependsOn(compileKotlin)
        dependsOn(compileJava)

        source(sourceSets["generated"].java)
        source(sourceSets["main"].java)

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }

}
