package com.bff.wespot.network.model.result

import android.util.Log
import com.bff.wespot.model.result.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

suspend inline fun <reified T> parseResponse(
    httpRequest: () -> HttpResponse,
): Result<T, ErrorDto> {
    return runCatching {
        val response = httpRequest()
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
}
