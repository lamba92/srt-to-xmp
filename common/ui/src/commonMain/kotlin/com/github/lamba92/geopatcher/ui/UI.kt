package com.github.lamba92.geopatcher.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


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
fun App(colors: ColorScheme) {
    MeasuredBox { (width) ->
        MaterialTheme(colors) {
            Surface(modifier = Modifier.fillMaxSize()) {
                val viewModel by getViewModel<AppViewModel>()
                val recentPaths by viewModel.recentPaths.collectAsState()
                val selectedPath by viewModel.selectedDirectory.collectAsState()
                ResponsiveNavigationDrawer(
                    drawerContent = {
                        DrawerContent(
                            modifier = Modifier.width(280.dp),
                            recentPaths = recentPaths,
                            onRecentSelected = { viewModel.selectPath(it) },
                            selectedPath = selectedPath,
                        )
                    },
                    isSmallScreen = width < 840,
                    drawerState = viewModel.drawerState,
                    content = {
                        Text("Hello World!")
                    },
                    verticalDividerModifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
fun LazyItemScope.VideoRow(
    file: AppFile.Multimedia.Video,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            .height(48.dp)
    ) {
//        VideoImageThumbnail(
//            file.path,
//        )
    }
}

@Composable
expect fun VideoImageThumbnail(path: String, modifier: Modifier = Modifier)