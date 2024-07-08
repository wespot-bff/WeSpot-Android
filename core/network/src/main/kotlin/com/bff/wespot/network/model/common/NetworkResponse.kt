package com.bff.wespot.network.model.common

import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

internal suspend fun <T> handleResponse(
    responseBlock: suspend () -> HttpResponse,
    successBlock: suspend (HttpResponse) -> T
): Result<T, NetworkError> {
    return try {
        val response = responseBlock()

        when (response.status.value) {

            in 200..299 -> {
                Result.Success(successBlock(response))
            }
            401 -> {
                Result.Error(NetworkError.UNAUTHORIZED)
            }
            408 -> {
                Result.Error(NetworkError.REQUEST_TIMEOUT)
            }
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    } catch (exception: Exception) {
        when (exception) {
            is UnresolvedAddressException -> Result.Error(NetworkError.NO_INTERNET)
            is SerializationException -> Result.Error(NetworkError.SERIALIZATION)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}