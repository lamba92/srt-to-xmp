package com.github.lamba92.srttoexif

import kotlin.math.abs


data class SRTMetadata(
    val latitude: Double? = null,
    val longitude: Double? = null,
)

// language=xml
fun SRTMetadata.toXML(): String {
    val (latitude, longitude) = convertCoordinates(latitude ?: 0.0, longitude ?: 0.0)
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <x:xmpmeta xmlns:x="adobe:ns:meta/" x:xmptk="XMP Core 5.0.0">
            <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
                <rdf:Description rdf:about="" xmlns:exif="http://ns.adobe.com/exif/1.0/">
                    <exif:GPSLatitude>$latitude</exif:GPSLatitude>
                    <exif:GPSLongitude>$longitude</exif:GPSLongitude>
                </rdf:Description>
            </rdf:RDF>
        </x:xmpmeta>
    """.trimIndent()
}

fun convertCoordinates(latitude: Double, longitude: Double): Pair<String, String> {
    val latDirection = if (latitude >= 0) "N" else "S"
    val lonDirection = if (longitude >= 0) "E" else "W"

    val formattedLatitude = "${abs(latitude)}$latDirection"
    val formattedLongitude = "${abs(longitude)}$lonDirection"

    return Pair(formattedLatitude, formattedLongitude)
}

private val latitudeRegex = """\[latitude\s*:\s*([^\]]+)\]""".toRegex()
private val longitudeRegex = """\[longitude\s*:\s*([^\]]+)\]""".toRegex()

fun parseMetadata(metadataString: String) = SRTMetadata(
    latitude = latitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
    longitude = longitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
)

expect class File

