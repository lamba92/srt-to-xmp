import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

extensions.findByType<KotlinProjectExtension>()?.sourceSets?.all {
    languageSettings {
        optIn("androidx.compose.foundation.ExperimentalFoundationApi")
        optIn("androidx.compose.runtime.InternalComposeApi")
        optIn("kotlin.io.path.ExperimentalPathApi")
        optIn("kotlinx.coroutines.DelicateCoroutinesApi")
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        optIn("kotlinx.coroutines.FlowPreview")
        optIn("kotlinx.serialization.ExperimentalSerializationApi")
    }
}