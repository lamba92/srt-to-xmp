package com.github.lamba92.geopatcher.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

val imagesExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
val videosExtensions = listOf("mp4", "mkv", "avi", "mov", "webm", "flv", "wmv", "3gp", "3g2")

abstract class ViewModel {
    protected abstract val viewModelScope: CoroutineScope
}

val LocalDI: CompositionLocal<KoinApplication> = compositionLocalOf { error("Not provided") }

@Composable
inline fun <reified T: ViewModel> getViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T = LocalDI.current.koin.get(qualifier, parameters)

object DIModules {
    val viewModel = module {
        single { AppViewModel(get(), get(), get()) }
    }
}