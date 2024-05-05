package com.github.lamba92.geopatcher

import java.nio.file.Path
import java.nio.file.attribute.FileTime
import kotlin.io.path.extension
import kotlin.io.path.fileSize
import kotlin.io.path.getAttribute
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import java.io.File
import kotlin.io.path.deleteExisting
import kotlin.io.path.deleteRecursively
import kotlin.io.path.moveTo
import java.nio.file.Path as NioPath
import kotlin.io.path.Path as NioPath

actual fun AppPath(path: String): AppPath = NioPath(path).toAppPath()

fun NioPath.toAppPath(): AppPath = when {
    endsWith("\\") || endsWith("/") || isDirectory() -> AppPathDirectoryImpl(this)
    else -> AppPathFileImpl(this)
}

actual fun AppPathDirectory(path: String): AppPath.Directory = AppPathDirectoryImpl(NioPath(path))
actual fun AppPathFile(path: String): AppPath.File = AppPathFileImpl(NioPath(path))

class AppPathFileImpl(val delegate: NioPath) : AppPath.File {
    override val size: FileSize
        get() = delegate.fileSize().bytes

    override val extension: String?
        get() = delegate.extension.takeIf { it.isNotEmpty() }

    override val name: String
        get() = delegate.name

    override val nameWithoutExtension: String
        get() = delegate.nameWithoutExtension

    override val parent: AppPath.Directory?
        get() = delegate.parent?.let { AppPathDirectoryImpl(it) }

    override val creationDate: Instant
        get() = delegate.getCreationTime()

    override val pathString: String
        get() = delegate.toString()

    override fun toAbsoluteAppPath(): AppPath.File = AppPathFileImpl(delegate.toAbsolutePath())

    override fun readLines() = delegate.inputStream().bufferedReader().lineSequence()

    override suspend fun readText() = withContext(Dispatchers.IO) {
        delegate.readText()
    }

    override suspend fun writeText(text: String) = withContext(Dispatchers.IO) {
        delegate.writeText(text)
    }

    override fun equals(other: Any?) = delegate == other
    override fun hashCode() = delegate.hashCode()

    override fun toString() = pathString

    override suspend fun delete() = withContext(Dispatchers.IO) {
        delegate.deleteExisting()
    }

    override suspend fun moveTo(input: AppPath) {
        withContext(Dispatchers.IO) {
            delegate.moveTo(
                when (input) {
                    is AppPathFileImpl -> input.delegate
                    is AppPathDirectoryImpl -> input.delegate
                    else -> error("Unsupported AppPath type")
            })
        }
    }
}

class AppPathDirectoryImpl(val delegate: NioPath) : AppPath.Directory {
    override val name: String
        get() = delegate.name

    override val nameWithoutExtension: String
        get() = delegate.nameWithoutExtension

    override val parent: AppPath.Directory?
        get() = delegate.parent?.let { AppPathDirectoryImpl(it) }

    override val creationDate: Instant
        get() = delegate.getCreationTime()

    override val pathString: String
        get() = delegate.toString()

    override fun toAbsoluteAppPath(): AppPath.Directory = AppPathDirectoryImpl(delegate.toAbsolutePath())

    override suspend fun listFiles(): List<AppPath> = withContext(Dispatchers.IO) {
        delegate.toFile()
            .listFiles()
            .orEmpty()
            .map { it.toPath().toAppPath() }
    }

    override fun resolve(path: String) = delegate.resolve(path).toAppPath()

    override fun equals(other: Any?) = delegate == other
    override fun hashCode() = delegate.hashCode()
    override fun toString() = pathString

    override suspend fun deleteRecursively() = withContext(Dispatchers.IO) {
        delegate.deleteRecursively()
    }

    override suspend fun moveTo(input: AppPath) {
        withContext(Dispatchers.IO) {
            delegate.moveTo(
                when (input) {
                    is AppPathFileImpl -> input.delegate
                    is AppPathDirectoryImpl -> input.delegate
                    else -> error("Unsupported AppPath type")
            })
        }

    }
}

private fun Path.getCreationTime(): Instant {
    val creationTime = getAttribute("creationTime") as FileTime
    return creationTime.toInstant().toKotlinInstant()
}

actual val FileSeparator: String = File.separator
