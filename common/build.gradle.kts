plugins {
    convention
    id(libs.plugins.jetbrains.kotlin.multiplatform)
    id(libs.plugins.kotlinx.serialization)
    id(libs.plugins.android.library)
}

kotlin {
    jvm()
    androidTarget()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlin.io.path.ExperimentalPathApi")
            }
        }

        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.serialization.core)
                api(libs.kotlinx.datetime)
            }
        }
        
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation(libs.junit.jupiter.api)
                runtimeOnly(libs.junit.jupiter.engine)
            }
        }

        val jvmCommonMain by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommonMain)
        }

        androidMain {
            dependsOn(jvmCommonMain)
            dependencies {
                api(libs.isoparser)
            }
        }
    }
}

android {
    namespace = "com.github.lamba92.geopatcher"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = false
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}