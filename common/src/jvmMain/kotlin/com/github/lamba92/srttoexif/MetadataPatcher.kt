package com.github.lamba92.srttoexif

import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class MetadataPatcher(private val exifToolsPath: Path) {

    actual suspend fun patchMetadata(input: File, metadata: SRTMetadata): Unit = withContext(Dispatchers.IO) {
        ProcessBuilder()
            .command(
                exifToolsPath.absolutePathString(),
                "-GPSLatitude=${metadata.latitude}",
                "-GPSLongitude=${metadata.longitude}",
                input.toPath().absolutePathString()
            )
            .start()
            .waitFor()
    }
}