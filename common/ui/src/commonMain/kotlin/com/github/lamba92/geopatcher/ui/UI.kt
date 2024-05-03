package com.github.lamba92.geopatcher.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.LocalSystemTheme
import androidx.compose.ui.SystemTheme.Dark

@Composable
fun App() {
    val colors = if (LocalSystemTheme.current == Dark) darkColorScheme() else lightColorScheme()
    val viewModel by getViewModel<AppViewModel>()
    MaterialTheme(colors) {
        ModalNavigationDrawer(
            drawerContent = {
                val recentPaths by viewModel.recentPaths.collectAsState()
                DrawerContent(
                    recentPaths = recentPaths,
                    onRecentSelected = { viewModel.selectPath(it) }
                )
            },
            content = {

            }
        )
    }
}
