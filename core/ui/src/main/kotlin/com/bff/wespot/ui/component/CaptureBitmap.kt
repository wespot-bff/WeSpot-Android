package com.bff.wespot.ui.component

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

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
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(WeSpotThemeManager.colors.backgroundColor),
                    ) {
                        content.invoke()
                    }
                }
            }
        },
    )

    return ::captureBitmap
}

fun saveBitmap(context: Context, bitmap: Bitmap, filename: String = DEFAULT_FILENAME): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }

    val contentResolver = context.contentResolver

    val imageUri: Uri? = contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues,
    )

    return imageUri.also {
        val fileOutputStream = imageUri?.let { contentResolver.openOutputStream(it) }
        if (fileOutputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        }
        fileOutputStream?.close()
    }
}

@JvmField
val DEFAULT_FILENAME = "${System.currentTimeMillis()}.png"
