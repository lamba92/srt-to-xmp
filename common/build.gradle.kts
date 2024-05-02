plugins {
    convention
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlin.io.path.ExperimentalPathApi")
            }
        }
        commonMain {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            }
        }
        
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
            }
        }
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}