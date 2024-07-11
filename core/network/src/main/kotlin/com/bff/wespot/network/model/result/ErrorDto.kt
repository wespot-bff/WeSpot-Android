package com.bff.wespot.network.model.result

import com.bff.wespot.model.result.BaseError
import com.bff.wespot.model.result.NetworkError
import kotlinx.serialization.Serializable

/**
    에러 발생 시, API는 에러 응답을 JSON 형태로 반환합니다.
    에러 응답에는 에러 유형(type), 에러 제목(title), HTTP 상태 코드(status), 상세 설명(detail), 에러 발생 위치(instance)가 포함됩니다.
 */
@Serializable
data class ErrorDto (
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String,
) : BaseError {
    constructor(): this(
        type = "",
        title = "",
        status = -1,
        detail = "UnKnownException",
        instance = "",
    )

    fun toError(): NetworkError = NetworkError(
        type = type,
        title = title,
        status = status,
        detail = detail,
        instance = instance,
    )
}