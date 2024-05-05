package com.github.lamba92.geopatcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.lamba92.geopatcher.AppPath
import com.github.lamba92.geopatcher.FileSeparator
import com.github.lamba92.geopatcher.resources.Res
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    recentPaths: List<AppPath.Directory>,
    onRecentSelected: (AppPath.Directory) -> Unit,
    selectedPath: AppPath.Directory?,
    drawerElevation: Dp = LocalAbsoluteTonalElevation.current
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerTonalElevation = drawerElevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "GeoPatcher",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            RecentMenu(recentPaths, selectedPath, onRecentSelected)
        }
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
        text = "Recent folders",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
    Spacer(Modifier.height(8.dp))
    for (path in recentPaths) {
        NavigationDrawerItem(
            label = {
                Row {
                    Icon(
                        painter = painterResource(Res.images.folder),
                        contentDescription = "Folder Icon",
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(path.truncateLeft(3))
                }
            },
            selected = path == selectedPath,
            onClick = { onRecentSelected(path) }
        )
    }
}

/*
 * This function keeps only the last `i` elements of the path.
 */
fun AppPath.truncateLeft(i: Int) = buildString {
    val split = pathString.split("/", "\\")
    if (split.size > i) {
        append("...")
        append(FileSeparator)
    }
    append(split.takeLast(i).joinToString(FileSeparator))
}
