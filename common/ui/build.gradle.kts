plugins {
    convention
    `kmp-resource-fix`
    id(libs.plugins.jetbrains.kotlin.multiplatform)
    id(libs.plugins.kotlinx.serialization)
    id(libs.plugins.compose.multiplatform)
    id(libs.plugins.moko.multiplatform.resources)
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
                api(libs.moko.resources)
                api(libs.moko.resources.compose)
                api(libs.jetbrains.material3)
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.serialization.json)
                api(libs.kodein.di)
            }
        }
    }
}
repositories {
    mavenCentral()
}

multiplatformResources {
    resourcesPackage = "com.github.lamba92.geopatcher.resources"
    resourcesClassName = "Res"
}
