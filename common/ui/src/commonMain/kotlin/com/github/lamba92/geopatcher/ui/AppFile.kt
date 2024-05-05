package com.github.lamba92.geopatcher.ui

import com.github.lamba92.geopatcher.AppPath
import com.github.lamba92.geopatcher.GeoCoordinates

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
            override val hasSrtFile: Boolean,
            val thumbnails: List<AppPath.File>
        ) : Multimedia
    }

    data class Srt(override val path: AppPath.File) : AppFile
    data class Directory(override val path: AppPath.Directory) : AppFile

}
