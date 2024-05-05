@file:Suppress("DEPRECATION")

import dev.icerock.gradle.tasks.GenerateMultiplatformResourcesTask
import java.util.Locale
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilationWithResources
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool

plugins.withId("org.jetbrains.kotlin.multiplatform") {
    with(extensions.getByType<KotlinMultiplatformExtension>()) {
        targets.all target@{
            if (this is KotlinMetadataTarget) return@target

            compilations.all compilation@{
                if (this !is KotlinCompilationWithResources) return@compilation
                tasks.named<ProcessResources>(processResourcesTaskName) {
                    from(allKotlinSourceSets.flatMap { it.resources.srcDirs })
                    duplicatesStrategy = DuplicatesStrategy.INCLUDE
                }
                tasks.named<KotlinCompile>(compileKotlinTaskName) {
                    doLast {
                        copy {
                            from(allKotlinSourceSets.flatMap { it.resources.srcDirs })
                            into(destinationDirectory)
                            duplicatesStrategy = DuplicatesStrategy.INCLUDE
                        }
                    }
                }
            }
        }
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
    ) else it.toString()
}