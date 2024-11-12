package com.bff.wespot.data.remote.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.bff.wespot.data.remote.model.ImageUploadFailedException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ImageDecoderDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageDecoderDataSource {
    override suspend fun decodeImage(imagePath: String): String {
        val uri = Uri.parse(imagePath)

        return resizeAndConvertToWebp(context, uri)
    }

    private fun resizeAndConvertToWebp(
        context: Context,
        uri: Uri,
        desiredWidth: Int = 1440,
        desiredHeight: Int = 1440,
    ): String {
        return run {
            val mimeType = context.contentResolver.getType(uri)
                ?: throw ImageUploadFailedException.ImageDecodeFailedException(
                    "Failed to get MIME type from URI: $uri"
                )
            val validMimeTypes = listOf("image/jpeg", "image/png", "image/webp")
            if (mimeType !in validMimeTypes) {
                throw ImageUploadFailedException.UnSupportedImageFormatException(
                    "Unsupported image format: $mimeType"
                )
            }

            val cacheDir = context.cacheDir
            val outputFile = File.createTempFile("resized_image_", ".webp", cacheDir)

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            options.inSampleSize = calculateInSampleSize(options, desiredWidth, desiredHeight)
            options.inJustDecodeBounds = false

            val resizedBitmap = context.contentResolver.openInputStream(uri)
                ?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream, null, options)
                } ?: throw ImageUploadFailedException.ImageDecodeFailedException(
                "Failed to decode bitmap from URI: $uri"
            )

            FileOutputStream(outputFile).use { outputStream ->
                if (!resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 80, outputStream)) {
                    throw ImageUploadFailedException.ImageDecodeFailedException(
                        "Failed to compress bitmap to WebP format."
                    )
                }
                outputStream.flush()
            }

            if (!outputFile.exists()) {
                throw ImageUploadFailedException.ImageDecodeFailedException(
                    "Failed to save the resized image."
                )
            }

            outputFile.absolutePath
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}