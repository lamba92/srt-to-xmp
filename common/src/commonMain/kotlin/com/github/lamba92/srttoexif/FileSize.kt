package com.github.lamba92.srttoexif

data class FileSize(val bytes: Long) : Comparable<FileSize> {

    init {
        require(bytes >= 0) { "File size cannot be negative!" }
    }

    val inKilobytes: Double get() = bytes / 1024.0
    val inMegabytes: Double get() = inKilobytes / 1024.0
    val inGigabytes: Double get() = inMegabytes / 1024.0

    override fun toString(): String = when {
        bytes < 1024 -> "${String.format(".2f", bytes)} B"
        bytes < 1024 * 1024 -> "${String.format(".2f", inKilobytes)} KB"
        else -> "${String.format(".2f", inGigabytes)} GB"
    }

    operator fun plus(other: FileSize) = FileSize(bytes + other.bytes)
    operator fun minus(other: FileSize) = FileSize(bytes - other.bytes)
    operator fun times(other: Number) = FileSize((bytes * other.toLong()))
    operator fun div(other: Number) = FileSize((bytes / other.toLong()))

    override fun compareTo(other: FileSize) = bytes.compareTo(other.bytes)
}

val Number.bytes
    get() = FileSize(toLong())

val Number.kilobytes
    get() = FileSize((toLong() * 1024))

val Number.megabytes
    get() = FileSize((toLong() * 1024 * 1024))

val Number.gigabytes
    get() = FileSize((toLong() * 1024 * 1024 * 1024))