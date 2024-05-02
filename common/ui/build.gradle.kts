plugins {
    convention
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.FlowPreview")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        commonMain {
            dependencies {
                api(projects.common)
                api("org.jetbrains.compose.desktop:desktop-jvm-linux-x64:1.6.2")
                api("org.jetbrains.compose.desktop:desktop-jvm-macos-arm64:1.6.2")
                api("org.jetbrains.compose.desktop:desktop-jvm-macos-x64:1.6.2")
                api("org.jetbrains.compose.desktop:desktop-jvm-windows-x64:1.6.2")
                api("org.jetbrains.compose.material3:material3:1.6.2")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
    }
}
repositories {
    mavenCentral()
}
