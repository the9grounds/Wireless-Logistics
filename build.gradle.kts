import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    java
    idea
    `maven-publish`
    id("net.neoforged.moddev") version "1.0.11"
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.24"
}

version = project.extra["mod_version"]!!
group = project.extra["mod_group_id"]!!
base.archivesName.set(project.extra["mod_id"]!!.toString())

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

neoForge {
    version = project.extra["neo_version"]!!.toString()
    accessTransformers {
        file("src/main/resources/META-INF/accesstransformer.cfg")
    }

    parchment {
        mappingsVersion = project.extra["parchment_mappings_version"]!!.toString()
        minecraftVersion = project.extra["parchment_minecraft_version"]!!.toString()
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel.set(org.slf4j.event.Level.DEBUG)
        }

        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["mod_id"]!!.toString())
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["mod_id"]!!.toString())
        }

        create("gameTestServer") {
            type.set("gameTestServer")
            systemProperty("neoforge.enabledGameTestNamespaces", project.extra["mod_id"]!!.toString())
        }

        create("data") {
            data()
            programArguments.addAll(
                listOf(
                    "--mod", project.extra["mod_id"]!!.toString(),
                    "--all",
                    "--output", file("src/generated/resources/").absolutePath,
                    "--existing", file("src/main/resources/").absolutePath
                )
            )
        }
    }

    mods {
        create(project.extra["mod_id"]!!.toString()) {
            sourceSet(sourceSets["main"])
        }
    }
}

sourceSets["main"].resources.srcDir("src/generated/resources")
sourceSets["main"].kotlin.srcDir("src/generated/ksp")

val copyKspOutput by tasks.registering(Copy::class) {
    from("build/generated/ksp/main/kotlin")
    into("src/generated/ksp")
}

val removeOldKsp by tasks.registering(Delete::class) {
    delete("build/generated/ksp/main/kotlin")
}

afterEvaluate {
    tasks.named("kspKotlin") {
        finalizedBy(copyKspOutput)
    }
    tasks.named("copyKspOutput") {
        finalizedBy(removeOldKsp)
    }
    tasks.named("compileKotlin") {
        dependsOn(removeOldKsp)
    }

//    tasks.named("compileKotlin") {
//        dependsOn("copyKspOutput")
//        inputs.dir("build/generated/ksp/main/kotlin")
//    }
}

configurations {
    create("localRuntime")
    getByName("runtimeClasspath").extendsFrom(getByName("localRuntime"))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
        content {
            includeGroup("thedarkcolour")
        }
    }

    maven {
        name = "Modmaven"
        url = uri("https://modmaven.dev/")
    }
}

dependencies {
    implementation("thedarkcolour:kotlinforforge-neoforge:${project.extra["kotlinForForgeVersion"]}")
    implementation(project(":optionalinterop-processor")) // for @OptionalInterop annotation
    ksp(project(":optionalinterop-processor"))

    implementation("org.appliedenergistics:appliedenergistics2:${project.extra["ae2Version"]}")
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to project.extra["minecraft_version"]!!,
        "minecraft_version_range" to project.extra["minecraft_version_range"]!!,
        "neo_version" to project.extra["neo_version"]!!,
        "neo_version_range" to project.extra["neo_version_range"]!!,
        "loader_version_range" to project.extra["loader_version_range"]!!,
        "mod_id" to project.extra["mod_id"]!!,
        "mod_name" to project.extra["mod_name"]!!,
        "mod_license" to project.extra["mod_license"]!!,
        "mod_version" to project.extra["mod_version"]!!,
        "mod_authors" to project.extra["mod_authors"]!!,
        "mod_description" to project.extra["mod_description"]!!
    )

    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
