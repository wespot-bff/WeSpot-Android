package com.bff.wespot.network.extensions

import android.util.Log
import com.bff.wespot.model.result.Result
import com.bff.wespot.network.model.result.ErrorDto
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.isSuccess

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T, ErrorDto> =
    runCatching {
        val response = request {
            block()
        }
        return if (response.status.isSuccess()) {
            val responseBody = response.body<T>()
            Result.Success(responseBody)
        } else {
            val errorBody = response.body<ErrorDto>()
            Result.Error(errorBody)
        }
    }.onFailure { exception ->
        Log.e("HttpClient", exception.message.toString())
    }.getOrDefault(
        Result.Error(ErrorDto())
    )