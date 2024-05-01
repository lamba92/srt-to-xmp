package com.github.lamba92.srttoexif

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName


data class SRTMetadata(

    @XmlSerialName("ISOSpeedRatings", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val iso: Int? = null,

    @XmlSerialName("ShutterSpeedValue", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val shutterSpeed: String? = null,

    @XmlSerialName("ApertureValue", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val apertureNumber: Double? = null,

    @XmlSerialName("ExposureBiasValue", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val exposureValue: Double? = null,

    @XmlSerialName("ColorTemperature", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val colorTemperature: Int? = null,

    @XmlSerialName("ColorMode", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val colorMode: String? = null,

    @XmlSerialName("FocalLength", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val focalLength: Double? = null,

    @XmlSerialName("DigitalZoomRatio", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val digitalZoomRatio: Double? = null,

    @XmlSerialName("Delta", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val delta: Int? = null,

    @XmlSerialName("GPSLatitude", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val latitude: Double? = null,

    @XmlSerialName("GPSLongitude", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val longitude: Double? = null,

    @XmlSerialName("GPSAltitudeRef", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val relativeAltitude: Double? = null,

    @XmlSerialName("GPSAltitude", namespace = "http://ns.adobe.com/exif/1.0/", prefix = "exif")
    @XmlElement(true)
    val absoluteAltitude: Double? = null
)


private val isoRegex = """\[iso\s*:\s*(\d+)\]""".toRegex()
private val shutterSpeedRegex = """\[shutter\s*:\s*([^\]]+)\]""".toRegex()
private val apertureNumberRegex = """\[fnum\s*:\s*(\d+)\]""".toRegex()
private val exposureValueRegex = """\[ev\s*:\s*([^\]]+)\]""".toRegex()
private val colorTemperatureRegex = """\[ct\s*:\s*(\d+)\]""".toRegex()
private val colorModeRegex = """\[color_md\s*:\s*([^\]]+)\]""".toRegex()
private val focalLengthRegex = """\[focal_len\s*:\s*(\d+)\]""".toRegex()
private val digitalZoomRatioRegex = """\[dzoom_ratio\s*:\s*(\d+)""".toRegex()
private val deltaRegex = """delta\s*:\s*(\d+)\]""".toRegex()
private val latitudeRegex = """\[latitude\s*:\s*([^\]]+)\]""".toRegex()
private val longitudeRegex = """\[longitude\s*:\s*([^\]]+)\]""".toRegex()
private val relativeAltitudeRegex = """\[rel_alt\s*:\s*([^\s]+)""".toRegex()
private val absoluteAltitudeRegex = """abs_alt\s*:\s*([^\]]+)\]""".toRegex()

fun parseMetadata(metadataString: String) = XMPMeta(
    XMPMeta.RDF(
        XMPMeta.RDF.SRTMetadata(
            iso = isoRegex.find(metadataString)?.groupValues?.get(1)?.toInt(),
            shutterSpeed = shutterSpeedRegex.find(metadataString)?.groupValues?.get(1),
            apertureNumber = apertureNumberRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            exposureValue = exposureValueRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            colorTemperature = colorTemperatureRegex.find(metadataString)?.groupValues?.get(1)?.toInt(),
            colorMode = colorModeRegex.find(metadataString)?.groupValues?.get(1),
            focalLength = focalLengthRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            digitalZoomRatio = digitalZoomRatioRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            delta = deltaRegex.find(metadataString)?.groupValues?.get(1)?.toInt(),
            latitude = latitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            longitude = longitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            relativeAltitude = relativeAltitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble(),
            absoluteAltitude = absoluteAltitudeRegex.find(metadataString)?.groupValues?.get(1)?.toDouble()
        )
    )
)

expect class File

expect suspend fun File.patchCoordinates(metadata: XMPMeta.RDF.SRTMetadata)
