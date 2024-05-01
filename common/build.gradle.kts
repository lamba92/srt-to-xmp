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
            }
        }
        commonMain {
            dependencies {
                api("io.github.pdvrieze.xmlutil:serialization:0.86.3")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
            }
        }
        
        jvmMain {
            dependencies {
                api("net.bramp.ffmpeg:ffmpeg:0.8.0")
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