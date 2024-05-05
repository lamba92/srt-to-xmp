package com.github.lamba92.geopatcher.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    isSmallScreen: Boolean,
    drawerModifier: Modifier = Modifier,
    verticalDividerModifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    val elevation by animateDpAsState(if (isSmallScreen) LocalAbsoluteTonalElevation.current else 0.dp)
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides elevation) {
        var isAnimationCompleted by remember(isSmallScreen) { mutableStateOf(false) }
        val scrimColor by animateColorAsState(if (isSmallScreen) scrimColor else Color.Transparent)
        LaunchedEffect(isSmallScreen) {
            when {
                isSmallScreen -> drawerState.close()
                else -> drawerState.open()
            }
            isAnimationCompleted = true
        }

        when {
            isSmallScreen || !isAnimationCompleted -> {
                ModalNavigationDrawer(
                    modifier = drawerModifier,
                    drawerState = drawerState,
                    scrimColor = scrimColor,
                    gesturesEnabled = gesturesEnabled,
                    drawerContent = drawerContent,
                    content = content
                )
            }

            else -> Row(
                modifier = drawerModifier
            ) {
                drawerContent()
                VerticalDivider(verticalDividerModifier)
                content()
            }
        }
    }
}