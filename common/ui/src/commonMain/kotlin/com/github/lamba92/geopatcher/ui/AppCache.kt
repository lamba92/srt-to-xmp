package com.github.lamba92.geopatcher.ui

import com.github.lamba92.geopatcher.AppPath
import kotlinx.serialization.Serializable

@Serializable
data class AppCache(
    val groupByType: Boolean,
    val sortedBy: FileSorting,
    val isSortingReversed: Boolean,
    val recentPaths: List<AppPath.Directory>
)