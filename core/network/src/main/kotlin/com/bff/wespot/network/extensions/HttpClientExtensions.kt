package com.bff.wespot.network.extensions

import com.bff.wespot.network.model.exception.NetworkException
import com.orhanobut.logger.Logger
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> =
    runCatching {
        val response = request {
            block()
        }
        return if (response.status.isSuccess()) {
            val responseBody = response.body<T>()
            Result.success(responseBody)
        } else {
            val errorBody = response.body<NetworkException>()
            Result.failure(errorBody)
        }
    }.getOrElse { exception ->
        Logger.e(exception.message.toString())
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