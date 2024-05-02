package com.github.lamba92.srttoxmp.ui

import kotlinx.serialization.Serializable

@Serializable
data class AppCache(
    val groupByType: Boolean,
    val sortedBy: FileSorting,
    val isSortingReversed: Boolean,
    val recentPaths: List<String>
)