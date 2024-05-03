package com.github.lamba92.geopatcher.desktop

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.singleWindowApplication
import com.github.lamba92.geopatcher.AppPath
import com.github.lamba92.geopatcher.MetadataPatcher
import com.github.lamba92.geopatcher.toAppPath
import com.github.lamba92.geopatcher.ui.App
import com.github.lamba92.geopatcher.ui.DIModules
import com.github.lamba92.geopatcher.ui.LocalDI
import kotlin.io.path.createTempDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun main() {
    val di = DI {
        import(DIModules.viewModel)
        import(desktopModule)
    }
    singleWindowApplication {
        CompositionLocalProvider(LocalDI provides di) {
            App()
        }
    }
}

val desktopModule = DI.Module("desktopModule") {
    bind<MetadataPatcher>() with singleton { MetadataPatcher(AppPath("exiftool")) }
    bind<CoroutineScope>() with singleton { GlobalScope }
    bind<AppPath.Directory>("cache_path") with singleton { createTempDirectory().toAppPath() as AppPath.Directory }
}
