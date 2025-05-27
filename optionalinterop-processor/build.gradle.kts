plugins {
    kotlin("jvm") version "2.0.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.24"
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.0-1.0.24")
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")
}

kotlin {
    jvmToolchain(21)
}