package com.github.lamba92.srttoexif

private val latitudeRegex = """\[latitude\s*:\s*([^]]+)]""".toRegex()
private val longitudeRegex = """\[longitude\s*:\s*([^]]+)]""".toRegex()

fun parseMetadataFromSRT(metadataString: String) = GeoCoordinates.Absolute(
    latitude = latitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble() ?: error("No latitude found!"),
    longitude = longitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble() ?: error("No longitude found!"),
)
