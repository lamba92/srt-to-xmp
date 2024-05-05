@file:Suppress("UnstableApiUsage")

plugins {
    convention
    id(libs.plugins.jetbrains.kotlin.jvm)
    id(libs.plugins.kotlinx.serialization)
    id(libs.plugins.compose.multiplatform)
    id(libs.plugins.moko.multiplatform.resources)
    `kotlin-optins`
}

kotlin {
    jvmToolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(projects.common.ui)
    implementation(libs.compose.desktop.jvm.linux.x64)
    implementation(libs.compose.desktop.jvm.macos.arm64)
    implementation(libs.compose.desktop.jvm.macos.x64)
    implementation(libs.compose.desktop.jvm.windows.x64)
    implementation(libs.jsystemthemedetector)
    implementation(libs.window.styler)
}

multiplatformResources {
    resourcesPackage = "com.github.lamba92.geopatcher.resources.desktop"
}