plugins {
    `kotlin-dsl`
}

dependencies {
    api(kotlin("gradle-plugin"))
    api(libs.android.gradle.plugin)
    api(libs.moko.resources.generator)
}