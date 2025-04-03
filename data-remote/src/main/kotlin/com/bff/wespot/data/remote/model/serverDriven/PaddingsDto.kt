package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.Paddings
import kotlinx.serialization.Serializable

@Serializable
data class PaddingsDto(
    val top: Int = 0,
    val start: Int = 0,
    val end: Int = 0,
    val bottom: Int = 0
) {
    fun toDomain() = Paddings(
        top = top,
        start = start,
        end = end,
        bottom = bottom
    )

    companion object {
        val None = PaddingsDto()
    }
}