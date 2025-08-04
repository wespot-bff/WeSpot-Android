package com.bff.wespot.data.remote.model.serverDriven.type

import com.bff.wespot.model.serverDriven.type.ColorType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ColorTypeDto {
    @Serializable
    @SerialName("Hex")
    data class HexTypeDto(
        @SerialName("value")
        val hexCode: String
    ) : ColorTypeDto()

    @Serializable
    @SerialName("Token")
    data class TokenTypeDto(
        @SerialName("value")
        val token: String
    ) : ColorTypeDto()

    fun toDomain(): ColorType = when (this) {
        is HexTypeDto -> ColorType.Hex(hexCode)
        is TokenTypeDto -> ColorType.Token(token)
    }
}