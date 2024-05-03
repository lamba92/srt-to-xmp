plugins {
    convention
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
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
}

multiplatformResources {
    resourcesPackage = "org.github.lamba92.geopatcher.resources.desktop"
}