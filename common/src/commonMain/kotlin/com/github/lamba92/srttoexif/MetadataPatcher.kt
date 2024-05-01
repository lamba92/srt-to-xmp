package com.github.lamba92.srttoexif

expect class MetadataPatcher {
    suspend fun patchMetadata(input: File, metadata: SRTMetadata)
}