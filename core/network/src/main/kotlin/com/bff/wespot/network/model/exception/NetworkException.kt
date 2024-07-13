package com.bff.wespot.network.model.exception

import kotlinx.serialization.Serializable

/**
    에러 발생 시, API는 에러 응답을 JSON 형태로 반환합니다.
    에러 응답에는 에러 유형(type), 에러 제목(title), HTTP 상태 코드(status), 상세 설명(detail), 에러 발생 위치(instance)가 포함됩니다.
 */
@Serializable
data class NetworkException (
    val type: String = "",
    val title: String = "UnKnown Error",
    val status: Int = -1,
    val detail: String = "알 수 없는 에러가 발생하였습니다.",
    val instance: String = "",
) : Exception() {
    fun toUnresolvedAddressException(): NetworkException = NetworkException(
        type = type,
        title = "No Internet",
        status = 1000,
        detail = "인터넷 연결을 확인해주세요.",
        instance = "internal"
    )

    fun toSerializationException(): NetworkException = NetworkException(
        type = type,
        title = "Serialization Error",
        status = 2000,
        detail = detail,
        instance = "internal"
    )
}
