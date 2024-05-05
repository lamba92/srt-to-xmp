plugins {
    convention
    `kmp-resource-fix`
    id(libs.plugins.jetbrains.kotlin.multiplatform)
    id(libs.plugins.kotlinx.serialization)
    id(libs.plugins.compose.multiplatform)
    id(libs.plugins.moko.multiplatform.resources)
    `kotlin-optins`
}

kotlin {
    jvm()
    sourceSets {
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
