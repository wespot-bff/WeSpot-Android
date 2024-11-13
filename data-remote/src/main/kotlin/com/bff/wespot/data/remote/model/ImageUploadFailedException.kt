package com.bff.wespot.data.remote.model

sealed class ImageUploadFailedException(
    override val message: String,
    override val cause: Throwable? = null
) : Throwable(message, cause) {
    data class ImageDecodeFailedException(
        override val message: String,
        override val cause: Throwable? = null
    ) : ImageUploadFailedException(message, cause)

    data class UploadFailedException(
        override val message: String,
        override val cause: Throwable? = null
    ) : ImageUploadFailedException(message, cause)

    data class UnSupportedImageFormatException(
        override val message: String,
        override val cause: Throwable? = null
    ) : ImageUploadFailedException(message, cause)
}