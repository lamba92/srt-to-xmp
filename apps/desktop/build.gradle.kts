@file:Suppress("UnstableApiUsage")

plugins {
    convention
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    jvmToolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
                optIn("androidx.compose.runtime.InternalComposeApi")
            }
        }
    }
}

dependencies {
    implementation(projects.common.ui)
    implementation("org.jetbrains.compose.desktop:desktop-jvm-linux-x64:1.6.2")
    implementation("org.jetbrains.compose.desktop:desktop-jvm-macos-arm64:1.6.2")
    implementation("org.jetbrains.compose.desktop:desktop-jvm-macos-x64:1.6.2")
    implementation("org.jetbrains.compose.desktop:desktop-jvm-windows-x64:1.6.2")
    implementation("com.github.Dansoftowner:jSystemThemeDetector:3.6")
    implementation("com.mayakapps.compose:window-styler:0.3.3-SNAPSHOT")
}

multiplatformResources {
    resourcesPackage = "com.github.lamba92.geopatcher.resources.desktop"
}