import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.jvm.tasks.Jar

buildscript {
    val kotlinVersion: String by project
    
    repositories {
        maven( url = "https://maven.minecraftforge.net" )
        mavenCentral()
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.+") {
            isChanging = true
        }

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        // OPTIONAL Gradle plugin for Kotlin Serialization
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}

val modBaseName: String by project
val kotlinVersion: String by project
val minecraftVersion: String by project
val mcpChannel: String by project
val mcpMappings: String by project

plugins {
    val kotlinVersion: String by project
    kotlin("jvm") version kotlinVersion
    java
}
apply {
    plugin("net.minecraftforge.gradle")
    plugin("kotlinx-serialization")
}

apply(from = "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-3.1.0.gradle")

project.group = "com.the9grounds.aeadditions"
base.archivesBaseName = "WirelessLogistics-${minecraftVersion}"

configure<UserDevExtension> {
    mappings(mcpChannel, mcpMappings)

    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

        }
        create("server") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
        }
        create("data") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            args("--mod", "wirelesslogistics", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources"))
        }
    }
}

dependencies {
    // Use the latest version of Minecraft Forge
    "minecraft"("net.minecraftforge:forge:1.18.2-40.0.4")
}

val Project.minecraft: UserDevExtension
    get() = extensions.getByName<UserDevExtension>("minecraft")

sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("src/generated/resources")
        }
    }
}

tasks.withType<Jar> {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", getBetterVersion())
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE

    baseName = "${modBaseName}-${getBetterVersion()}"

    // replace stuff in mcmod.info, nothing else
    filesMatching("META-INF/mods.toml") {
        expand(mapOf(
            "version" to getBetterVersion(),
            "mcversion" to "1.18.2"
        ))
        filter { line ->
            line.replace("version=\"0.0.0.0.1\"", "version=\"${getBetterVersion()}\"")
        }
    }
}

fun getBuildNumber(): String? {

    if (System.getenv("CI") == null) {
        return "0.0.0.1"
    }

    if (System.getenv("TAG") != null) {
        return null
    }

    if (System.getenv("GITHUB_HEAD_REF") != null) {
        return "0.0.1-pr-${System.getenv("GITHUB_HEAD_REF")}-${System.getenv("SHORT_SHA")}"
    }

    return "0.0.1-ci-${System.getenv("BRANCH_NAME")}-${System.getenv("SHORT_SHA")}"
}

fun getBetterVersion(): String {
    val buildNumber = getBuildNumber()

    if (buildNumber == null) {
        val tag = System.getenv("TAG")

        return "${tag}"
    }

    return buildNumber
}

fun getReleaseType(): String {
    val preReleaseEnv = System.getenv("PRERELEASE")

    if (preReleaseEnv == null) {
        return "beta"
    }

    val preRelease = preReleaseEnv.toBoolean()

    if (preRelease) {
        return "beta"
    }

    return "release"
}