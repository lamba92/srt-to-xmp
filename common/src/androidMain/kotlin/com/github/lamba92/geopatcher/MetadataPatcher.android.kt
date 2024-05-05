@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.lamba92.geopatcher

import com.coremedia.iso.IsoFile
import com.coremedia.iso.boxes.MetaBox
import com.coremedia.iso.boxes.MovieBox
import com.coremedia.iso.boxes.UserDataBox
import com.coremedia.iso.boxes.XmlBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import com.googlecode.mp4parser.util.Path as Mp4parserUtilPath

actual class MetadataPatcher {
    actual suspend fun patchMetadata(
        input: AppPath.File,
        metadata: GeoCoordinates
    ) {
        val absolute = metadata.asAbsolute()
        when (input.extension) {
            in listOf("mp4", "mov") -> editMp4FileMetadata(input, absolute)

            else -> throw IllegalArgumentException("Unsupported file format")
        }

    }

    actual suspend fun readMetadata(input: AppPath.File): GeoCoordinates? =
        withContext(Dispatchers.IO) {
            when (input.extension) {
                in listOf("mp4", "mov") -> readLocationMetadataFromMp4(input.pathString)
                else -> null
            }
        }

}

suspend fun editMp4FileMetadata(
    input: AppPath.File,
    absolute: GeoCoordinates.Absolute
) {
    val outputFilePath = input.parent?.resolve("patched_${input.name}")
        ?: error("Cannot resolve output file path")
    withContext(Dispatchers.IO) {
        writeLocations(input, absolute, outputFilePath)
    }
    // replace file
    input.delete()
    outputFilePath.moveTo(input)
}


fun writeLocations(
    input: AppPath.File,
    absolute: GeoCoordinates.Absolute,
    outputFilePath: AppPath
) {
    val isoFile = IsoFile(input.pathString)
    val moov = isoFile.getBoxes(MovieBox::class.java).firstOrNull()
        ?: error("Cannot find moov box")
    val udta = moov.getBoxes(UserDataBox::class.java).firstOrNull()
        ?: UserDataBox().also { moov.addBox(it) }
    val meta = udta.getBoxes(MetaBox::class.java).firstOrNull()
        ?: MetaBox().also { udta.addBox(it) }

    val xmlString =
        """
            <gps xmlns="http://www.apple.com/quicktime/metadata" version="1">
                <latitude>${absolute.latitude}</latitude>
                <longitude>${absolute.longitude}</longitude>
            </gps>
        """.trimIndent()

    val xmlBox = XmlBox()
    xmlBox.xml = xmlString
    meta.addBox(xmlBox)

    FileOutputStream(outputFilePath.pathString).channel.use { fileChannel ->
        isoFile.getBox(fileChannel)
        fileChannel.force(true)
    }
    isoFile.close()
}

fun readLocationMetadataFromMp4(inputFilePath: String) =
    IsoFile(inputFilePath).use { isoFile ->
        val moov = Mp4parserUtilPath.getPath(isoFile, "/moov") as MovieBox
        val udta = moov.getBoxes(UserDataBox::class.java).firstOrNull()

        val meta = udta?.getBoxes(MetaBox::class.java)?.firstOrNull()
        val xmlBox = meta?.getBoxes(XmlBox::class.java)?.firstOrNull()

        xmlBox?.let { extractCoordinatesWithRegex(it.xml) }
    }


val latitudePattern = Regex("<latitude>([^<]+)</latitude>")
val longitudePattern = Regex("<longitude>([^<]+)</longitude>")

fun extractCoordinatesWithRegex(xml: String): GeoCoordinates.Absolute? {
    return GeoCoordinates.Absolute(
        latitude = latitudePattern.find(xml)?.groups?.get(1)?.value?.toDoubleOrNull()
            ?: return null,
        longitude = longitudePattern.find(xml)?.groups?.get(1)?.value?.toDoubleOrNull()
            ?: return null
    )
}
