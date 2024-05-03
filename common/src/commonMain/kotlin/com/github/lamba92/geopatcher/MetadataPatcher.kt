@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.lamba92.geopatcher

expect class MetadataPatcher {
    suspend fun patchMetadata(input: AppPath, metadata: GeoCoordinates)
    suspend fun readMetadata(input: AppPath): GeoCoordinates?
}