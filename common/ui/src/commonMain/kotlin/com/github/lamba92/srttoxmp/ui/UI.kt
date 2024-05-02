package com.github.lamba92.srttoxmp.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun App(isDark: Boolean = false) {
    val colors = if (isDark) darkColorScheme() else lightColorScheme()
    MaterialTheme(colors) {
        ModalNavigationDrawer(
            drawerContent = {
                /* Drawer content */
            },
            content = {

            }
        )
    }
}
