package com.bff.wespot.model.serverDriven.type

sealed interface ColorType {
    data class Hex(
        val hexCode: String,
    ) : ColorType

    data class Token(
        val token: String,
    ) : ColorType
}

data class GradationType(
    val startColor: ColorType,
    val endColor: ColorType,
    val angle: Int,
)
