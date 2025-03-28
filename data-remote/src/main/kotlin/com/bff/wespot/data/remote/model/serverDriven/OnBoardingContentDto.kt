package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.data.remote.model.serverDriven.section.BaseSectionDto
import com.bff.wespot.model.serverDriven.OnBoarding
import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingDto(
    val id: Int,
    val name: String,
    val data: List<BaseSectionDto>
) {
    fun toDomain() = OnBoarding(
        id = id,
        name = name,
        data = data.map { it.toDomain() }
    )
}