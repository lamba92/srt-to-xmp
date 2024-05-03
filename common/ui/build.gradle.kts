plugins {
    convention
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.coroutines.FlowPreview")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                optIn("androidx.compose.runtime.InternalComposeApi")
            }
        }
        commonMain {
            dependencies {
                api(projects.common)
                api("io.insert-koin:koin-core:3.5.6")
                api("dev.icerock.moko:resources:0.24.0-beta-2")
                api("dev.icerock.moko:resources-compose:0.24.0-beta-2")
                api("org.jetbrains.compose.material3:material3:1.6.2")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                api("dev.icerock.moko:mvvm-compose:0.16.1")
            }
        }
    }
}

multiplatformResources {
    resourcesPackage = "org.github.lamba92.geopatcher.resources"
    resourcesClassName = "Res"
}
