package com.github.lamba92.srttoexif

import com.github.lamba92.srttoexif.GeoCoordinates.Relative.LatitudeDirection.*
import com.github.lamba92.srttoexif.GeoCoordinates.Relative.LongitudeDirection.*

sealed interface GeoCoordinates {
    data class Absolute(
        val latitude: Double,
        val longitude: Double,
    ) : GeoCoordinates
    data class Relative(
        val latitude: Double,
        val longitude: Double,
        val latitudeDirection: LatitudeDirection,
        val longitudeDirection: LongitudeDirection,
    ) : GeoCoordinates {
        enum class LatitudeDirection { NORTH, SOUTH }
        enum class LongitudeDirection { EAST, WEST }
    }
}

fun GeoCoordinates.asAbsolute(): GeoCoordinates.Absolute = when (this) {
    is GeoCoordinates.Absolute -> this
    is GeoCoordinates.Relative -> GeoCoordinates.Absolute(
        latitude = latitude * if (latitudeDirection == NORTH) 1 else -1,
        longitude = longitude * if (longitudeDirection == EAST) 1 else -1
    )
}

fun GeoCoordinates.asRelative(): GeoCoordinates.Relative = when (this) {
    is GeoCoordinates.Absolute -> GeoCoordinates.Relative(
        latitude = latitude,
        longitude = longitude,
        latitudeDirection = if (latitude >= 0) NORTH else SOUTH,
        longitudeDirection = if (longitude >= 0) EAST else WEST
    )
    is GeoCoordinates.Relative -> this
}