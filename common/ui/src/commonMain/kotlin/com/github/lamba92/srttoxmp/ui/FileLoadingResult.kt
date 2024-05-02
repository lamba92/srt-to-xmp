package com.github.lamba92.srttoxmp.ui

sealed interface FileLoadingResult {
    data class Error(val error: Throwable) : FileLoadingResult
    data class Loaded(val files: List<AppFile>) : FileLoadingResult
}