package com.github.lamba92.geopatcher.ui

sealed interface FileLoadingResult {
    data class Error(val error: Throwable) : FileLoadingResult
    data class Loaded(val files: List<AppFile>) : FileLoadingResult
}