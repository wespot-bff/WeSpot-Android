package com.bff.wespot.network.extensions

import com.bff.wespot.network.model.exception.NetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.isSuccess
import io.ktor.util.AttributeKey
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import timber.log.Timber

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> =
    runCatching {
        val response = request {
            block()
        }
        return if (response.status.isSuccess()) {
            val parsedBody = response.call.attributes.getOrNull(AttributeKey("parsedBody"))
            val responseBody = response.body<T>()
            Result.success(parsedBody as? T ?: responseBody)
        } else {
            val errorBody = response.body<NetworkException>()
            Result.failure(errorBody)
        }
    }.getOrElse { exception ->
        Timber.e(exception)
        when (exception) {
            is SerializationException -> {
                Result.failure(NetworkException().toSerializationException())
            }

            is UnresolvedAddressException -> {
                Result.failure(NetworkException().toUnresolvedAddressException())
            }

            else -> {
                Result.failure(NetworkException())
            }
        }
    }