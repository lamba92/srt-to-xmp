package com.github.lamba92.geopatcher.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.lamba92.geopatcher.AppPath

@Composable
fun DrawerContent(
    recentPaths: List<AppPath.Directory>,
    onRecentSelected: (AppPath.Directory) -> Unit,
    selectedPath: AppPath.Directory?
) {
    ModalDrawerSheet {
        RecentMenu(recentPaths, selectedPath, onRecentSelected)
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
fun ColumnScope.RecentMenu(
    recentPaths: List<AppPath.Directory>,
    selectedPath: AppPath.Directory?,
    onRecentSelected: (AppPath.Directory) -> Unit
) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Recent files",
        fontWeight = FontWeight.Bold
    )
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth()
    )
    for (path in recentPaths) {
        NavigationDrawerItem(
            label = { Text(path.pathString) },
            selected = path == selectedPath,
            onClick = { onRecentSelected(path) }
        )
    }
}