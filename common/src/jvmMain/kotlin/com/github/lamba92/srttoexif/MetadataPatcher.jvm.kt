@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.lamba92.srttoexif

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class MetadataPatcher(private val exifToolsPath: AppPath) {

    companion object {
        /*
         * Example output:
         * [File] GPS Coordinates: 40.6892, -74.0445, 0
         * where the first two numbers are latitude and longitude respectively.
         */
        val OUTPUT_METADATA_REGEX = Regex(".*: (.*?) (.*?) (.*)")
    }

    // example:
    // -Keys:GPSCoordinates="40.6892, -74.0445, 0" -Userdata:GPSCoordinates="40.6892, -74.0445, 0"
    //      -Itemlist:GPSCoordinates="40.6892, -74.0445, 0"
    actual suspend fun patchMetadata(input: AppPath, metadata: GeoCoordinates): Unit = withContext(Dispatchers.IO) {
        val absolute = metadata.asAbsolute()
        ProcessBuilder()
            .command(
                exifToolsPath.toAbsolutePath().pathString,
                "-Keys:GPSCoordinates=\"${absolute.latitude}, ${absolute.longitude}, 0\"",
                "-Userdata:GPSCoordinates=\"${absolute.latitude}, ${absolute.longitude}, 0\"",
                "-Itemlist:GPSCoordinates=\"${absolute.latitude}, ${absolute.longitude}, 0\"",
                input.toAbsolutePath().pathString,
            )
            .start()
            .waitFor()
    }

    actual suspend fun readMetadata(input: AppPath): GeoCoordinates? = withContext(Dispatchers.IO) {
        val process = ProcessBuilder()
            .command(
                exifToolsPath.toAbsolutePath().pathString,
                "-GPSCoordinates",
                "-n",
                input.toAbsolutePath().pathString,
            )
            .start()
        val output = process.inputStream.bufferedReader().readText()
        val groups = OUTPUT_METADATA_REGEX.find(output)?.groupValues ?: return@withContext null
        if (groups.size < 3) return@withContext null
        process.waitFor()
        GeoCoordinates.Absolute(
            latitude = groups[1].toDouble(),
            longitude = groups[2].toDouble()
        )
    }

}