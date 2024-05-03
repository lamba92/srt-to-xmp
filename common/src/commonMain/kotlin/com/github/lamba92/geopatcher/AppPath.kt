package com.github.lamba92.geopatcher

import kotlinx.datetime.Instant

sealed interface AppPath {

    val name: String
    val nameWithoutExtension: String
    val parent: Directory?
    val creationDate: Instant
    val pathString: String

    fun toAbsolutePath(): AppPath

    interface File : AppPath {
        val size: FileSize
        val extension: String?

        override fun toAbsolutePath(): File

        fun readLines(): Sequence<String>

        suspend fun readText(): String
        suspend fun writeText(text: String)
    }

    interface Directory : AppPath {
        suspend fun listFiles(): List<AppPath>
        fun resolve(path: String): AppPath
        override fun toAbsolutePath(): Directory
    }

}

expect fun AppPath(path: String): AppPath

