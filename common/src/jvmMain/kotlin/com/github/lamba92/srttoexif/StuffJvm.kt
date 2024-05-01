package com.github.lamba92.srttoexif

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


actual typealias File = java.io.File

actual suspend fun File.patchCoordinates(metadata: XMPMeta.RDF.SRTMetadata): Unit = withContext(Dispatchers.IO) {
    TODO()
}


