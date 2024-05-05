package com.github.lamba92.geopatcher.desktop

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.singleWindowApplication
import com.github.lamba92.geopatcher.AppPath
import com.github.lamba92.geopatcher.MetadataPatcher
import com.github.lamba92.geopatcher.toAppPath
import com.github.lamba92.geopatcher.ui.App
import com.github.lamba92.geopatcher.ui.DIModules
import com.github.lamba92.geopatcher.ui.LocalDI
import com.jthemedetecor.OsThemeDetector
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowStyle
import java.util.function.Consumer
import kotlin.io.path.createTempDirectory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

fun main() {
    val di = DI {
        import(DIModules.viewModel)
        import(desktopModule)
    }
    singleWindowApplication {
        val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()
        WindowStyle(
            isDarkTheme = isDarkTheme,
            backdropType = WindowBackdrop.Solid(colors.surface),
        )
        CompositionLocalProvider(
            LocalDI provides di,
        ) {
            App(colors)
        }
    }
}

val desktopModule = DI.Module("desktopModule") {
    bind<MetadataPatcher>() with singleton { MetadataPatcher(AppPath("exiftool")) }
    bind<CoroutineScope>() with singleton { GlobalScope }
    bind<AppPath.Directory>("cache_path") with singleton { createTempDirectory().toAppPath() as AppPath.Directory }
}

val isDarkTheme
    @Composable
    get() = isDarkFlow.collectAsState(OsThemeDetector.getDetector().isDark).value

val isDarkFlow: Flow<Boolean>
    get() = callbackFlow {
        val detector = OsThemeDetector.getDetector()
        val listener = Consumer<Boolean> { trySend(it) }
        detector.registerListener(listener)
        awaitClose { detector.removeListener(listener) }
    }