package com.github.lamba92.geopatcher.desktop

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.singleWindowApplication
import com.github.lamba92.geopatcher.AppPath
import com.github.lamba92.geopatcher.MetadataPatcher
import com.github.lamba92.geopatcher.ui.App
import com.github.lamba92.geopatcher.ui.DIModules
import com.github.lamba92.geopatcher.ui.LocalDI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.koinApplication
import org.koin.dsl.module

fun main() {
    val koin = koinApplication {
        modules(DIModules.viewModel)
        module {
            single<CoroutineScope> { GlobalScope }
            single { MetadataPatcher(AppPath("exiftool")) }
        }
    }
    singleWindowApplication {
        CompositionLocalProvider(LocalDI provides koin) {
            App()
        }
    }
}