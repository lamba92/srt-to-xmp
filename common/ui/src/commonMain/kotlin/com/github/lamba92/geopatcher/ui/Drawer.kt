package com.github.lamba92.geopatcher.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.github.lamba92.geopatcher.AppPath
import dev.icerock.moko.resources.compose.painterResource
import org.github.lamba92.geopatcher.resources.Res

@Composable
fun DrawerContent(
    recentPaths: List<AppPath.Directory>,
    onRecentSelected: (AppPath.Directory) -> Unit
) {
    val logo = painterResource(Res.images.logo)
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = logo,
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Divider(modifier = Modifier.fillMaxWidth())
        RecentMenu(recentPaths, onRecentSelected)
    }
}

@Suppress("UnusedReceiverParameter")
@Composable
fun ColumnScope.RecentMenu(
    recentPaths: List<AppPath.Directory>,
    onRecentSelected: (AppPath.Directory) -> Unit
) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Recent files",
        fontWeight = FontWeight.Bold
    )
    Divider(
        modifier = Modifier.fillMaxWidth()
    )
    for (path in recentPaths) {
        ClickableText(
            text = buildAnnotatedString {
                append(path.pathString)
            },
            onHover = {},
            modifier = Modifier.fillMaxWidth(),
            onClick = { onRecentSelected(path) }
        )
    }
}