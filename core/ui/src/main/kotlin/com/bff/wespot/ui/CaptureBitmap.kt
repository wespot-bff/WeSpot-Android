package com.bff.wespot.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun CaptureBitmap(
    content: @Composable () -> Unit,
): () -> Bitmap {
    val context = LocalContext.current

    val composeView = remember {
        ComposeView(context)
    }

    fun captureBitmap(): Bitmap {
        return composeView.drawToBitmap()
    }

    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        },
    )

    return ::captureBitmap
}

suspend fun saveImage(image: Bitmap, context: Context): Uri? =
    withContext(Dispatchers.IO) {
        val imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null

        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            FileOutputStream(file).use { stream ->
                if (image.compress(Bitmap.CompressFormat.PNG, 90, stream)) {
                    stream.flush()
                    uri = FileProvider.getUriForFile(context, "com.bff.wespot.fileProvider", file)
                } else {
                    Timber.e("Error", "Image compression failed")
                }
            }
        } catch (e: IOException) {
            Timber.e("Error", "IOException while trying to write file for sharing: ${e.message}")
        }

        uri
    }