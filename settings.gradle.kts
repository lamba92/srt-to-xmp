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
        kotlin("jvm") version kotlinVersion
        kotlin("android") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jetbrains.compose") version "1.6.2"
        id("com.android.application") version "8.4.0"
        id("dev.icerock.mobile.multiplatform-resources") version "0.24.0-beta-2"
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