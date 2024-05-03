package com.github.lamba92.geopatcher.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val imagesExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
val videosExtensions = listOf("mp4", "mkv", "avi", "mov", "webm", "flv", "wmv", "3gp", "3g2")

abstract class ViewModel {
    protected abstract val viewModelScope: CoroutineScope
}

val LocalDI: ProvidableCompositionLocal<DI> = staticCompositionLocalOf { error("Not provided") }

@Composable
inline fun <reified T : ViewModel> getViewModel() =
    LocalDI.current.instance<T>()

object DIModules {
    val viewModel
        get() = DI.Module("viewModel") {
            bind<AppViewModel>() with singleton { AppViewModel(instance(), instance("cache_path"), instance()) }
        }
}