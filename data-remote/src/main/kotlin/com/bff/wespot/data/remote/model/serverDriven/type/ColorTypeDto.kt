package com.bff.wespot.data.remote.model.serverDriven.type

import com.bff.wespot.model.serverDriven.type.ColorType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ColorTypeDto {
    @Serializable
    @SerialName("hex")
    data class HexTypeDto(
        val hexCode: String
    ) : ColorTypeDto()

    @Serializable
    @SerialName("token")
    data class TokenTypeDto(
        val token: String
    ) : ColorTypeDto()

    fun toDomain(): ColorType = when (this) {
        is HexTypeDto -> ColorType.Hex(hexCode)
        is TokenTypeDto -> ColorType.Token(token)
    }
}