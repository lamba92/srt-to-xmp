package com.github.lamba92.srttoxmp.ui

import com.github.lamba92.srttoexif.AppPath
import com.github.lamba92.srttoexif.GeoCoordinates

sealed interface AppFile {

    val path: AppPath

    sealed interface Multimedia : AppFile {

        val location: GeoCoordinates?
        val hasSrtFile: Boolean
        override val path: AppPath.File

        data class Image(
            override val path: AppPath.File,
            override val location: GeoCoordinates?,
            override val hasSrtFile: Boolean
        ) : Multimedia

        data class Video(
            override val path: AppPath.File,
            override val location: GeoCoordinates?,
            override val hasSrtFile: Boolean
        ) : Multimedia
    }

    data class Srt(override val path: AppPath.File) : AppFile
    data class Directory(override val path: AppPath.Directory) : AppFile

}