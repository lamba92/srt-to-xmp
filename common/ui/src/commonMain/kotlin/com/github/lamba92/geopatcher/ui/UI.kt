package com.github.lamba92.geopatcher.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.LocalSystemTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.SystemTheme.Dark
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize


@Composable
fun MeasuredBox(modifier: Modifier = Modifier, content: @Composable (IntSize) -> Unit) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = modifier.onSizeChanged { size = it }
    ) {
        content(size)
    }
}

@Composable
fun App(colors: ColorScheme) = MeasuredBox { (width) ->
    MaterialTheme(colors) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val viewModel by getViewModel<AppViewModel>()
            val recentPaths by viewModel.recentPaths.collectAsState()
            val selectedPath by viewModel.selectedDirectory.collectAsState()
            val isSmallScreen = width < 840
            LaunchedEffect(isSmallScreen) {
                when {
                    isSmallScreen -> viewModel.drawerState.close()
                    else -> viewModel.drawerState.open()
                }
            }

            when {
                isSmallScreen -> ModalNavigationDrawer(
                    modifier = Modifier.fillMaxSize(),
                    drawerState = viewModel.drawerState,
                    drawerContent = {
                        DrawerContent(
                            recentPaths = recentPaths,
                            onRecentSelected = { viewModel.selectPath(it) },
                            selectedPath = selectedPath
                        )
                    },
                    content = {
                        Text("Hello, World!")
                    }
                )

                else -> Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DrawerContent(
                        recentPaths = recentPaths,
                        onRecentSelected = { viewModel.selectPath(it) },
                        selectedPath = selectedPath
                    )
                    VerticalDivider()
                    Text("Hello, World!")
                }
            }
        }
    }
}

@Composable
private fun isScreenSmall(size: IntSize) = size.width < 840 * LocalDensity.current.density
