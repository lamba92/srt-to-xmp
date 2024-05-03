@file:Suppress("UnstableApiUsage")

rootProject.name = "geopatcher"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion = "1.9.23"
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jetbrains.compose") version "1.6.2"
        id("com.android.application") version "8.4.0"
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}


include(
    ":common",
    ":common:ui",
    ":apps",
    ":apps:android",
    ":apps:desktop"
)