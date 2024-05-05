package com.github.lamba92.geopatcher

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = AppPath.Companion::class)
sealed interface AppPath {

    companion object : KSerializer<AppPath> {
        override val descriptor: SerialDescriptor
            get() = String.serializer().descriptor

        override fun deserialize(decoder: Decoder) = AppPath(decoder.decodeString())

        override fun serialize(encoder: Encoder, value: AppPath) {
            encoder.encodeString(value.pathString)
        }
    }

    val name: String
    val nameWithoutExtension: String
    val parent: Directory?
    val creationDate: Instant
    val pathString: String

    fun toAbsoluteAppPath(): AppPath
    suspend fun moveTo(input: AppPath)

    @Serializable(with = File.Companion::class)
    interface File : AppPath {

        companion object : KSerializer<File> {
            override val descriptor: SerialDescriptor
                get() = String.serializer().descriptor

            override fun deserialize(decoder: Decoder) = AppPath(decoder.decodeString()) as File

            override fun serialize(encoder: Encoder, value: File) {
                encoder.encodeString(value.pathString)
            }
        }

        val size: FileSize
        val extension: String?

        override fun toAbsoluteAppPath(): File

        fun readLines(): Sequence<String>

        suspend fun readText(): String
        suspend fun writeText(text: String)
        suspend fun delete()
    }

    @Serializable(with = Directory.Companion::class)
    interface Directory : AppPath {

        companion object : KSerializer<Directory> {
            override val descriptor: SerialDescriptor
                get() = String.serializer().descriptor

            override fun deserialize(decoder: Decoder) = AppPath(decoder.decodeString()) as Directory

            override fun serialize(encoder: Encoder, value: Directory) {
                encoder.encodeString(value.pathString)
            }
        }

        suspend fun listFiles(): List<AppPath>
        fun resolve(path: String): AppPath
        override fun toAbsoluteAppPath(): Directory
        suspend fun deleteRecursively()
    }

}

expect fun AppPath(path: String): AppPath
