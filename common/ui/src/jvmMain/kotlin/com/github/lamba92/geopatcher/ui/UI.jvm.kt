package com.github.lamba92.geopatcher.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import kotlin.time.Duration.Companion.microseconds

@Composable
actual fun VideoImageThumbnail(path: String, modifier: Modifier) {
    var grabber by remember(path) { mutableStateOf<FFmpegFrameGrabber?>(null) }
    val scope = rememberCoroutineScope()
    var converter: Java2DFrameConverter? by remember { mutableStateOf(null) }
    DisposableEffect(path) {
        println("Creating grabber -> $path")
        scope.launch(Dispatchers.IO) {
            grabber = FFmpegFrameGrabber(path)
                .apply {
                    setOption("hwaccel", "nvdec")
                    setOption("hwaccel_device", "0")
                    start()
                }
            converter = Java2DFrameConverter()
        }
        onDispose {
            println("Disposing grabber -> $path")
            val previousGrabber = grabber
            val previousConverter = converter
            GlobalScope.launch {
                previousGrabber?.stop()
                previousGrabber?.release()
                previousConverter?.close()
            }
        }
    }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var mouseLocation by remember { mutableStateOf(Offset.Unspecified) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    remember(mouseLocation) {
        grabber?.let {
            val originalWidth = it.imageWidth
            val originalHeight = it.imageHeight

            // Calculate new dimensions to preserve aspect ratio
            val aspectRatio = originalWidth.toDouble() / originalHeight.toDouble()

            // Compare the aspect ratios to decide on dimensions
            println(boxSize)
            val (scaledWidth, scaledHeight) = when {
                boxSize.width / boxSize.height > aspectRatio -> {
                    // Target is wider than the original
                    boxSize.height to (boxSize.height * aspectRatio).toInt()
                }

                else -> {
                    // Target is taller than the original
                    boxSize.width to (boxSize.width / aspectRatio).toInt()
                }
            }
            it.imageWidth = scaledWidth
            it.imageHeight = scaledHeight

            if (mouseLocation != Offset.Unspecified) {
                val mouseOffsetPercent = mouseLocation.x.toDouble() / boxSize.width
                val videoOffset = it.lengthInTime * mouseOffsetPercent
                it.timestamp = videoOffset.microseconds.inWholeMicroseconds
            }
        }
        val toComposeImageBitmap = converter?.convert(grabber?.grabImage())
            ?.toComposeImageBitmap()
        bitmap = toComposeImageBitmap
    }
    Box(
        modifier = modifier
            .onSizeChanged { boxSize = it }
            .onPointerEvent(PointerEventType.Move) {
                it.changes.firstOrNull()?.position?.let {
                    mouseLocation = it
                }
            }
    ) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
