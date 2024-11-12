package com.bff.wespot.data.remote.source

interface ImageDecoderDataSource {
    suspend fun decodeImage(imagePath: String): String
}