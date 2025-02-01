package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.OnBoarding
import com.bff.wespot.model.serverDriven.OnBoardingContent
import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingDto(
    val id: Int,
    val name: String,
    val data: List<OnBoardingContentDto>
) {
    fun toDomain() = OnBoarding(
        id = id,
        name = name,
        data = data.map { it.toDomain() }
    )
}

@Serializable
data class OnBoardingContentDto(
    val page: Int,
    val data: List<BaseComponentDto>
) {
    fun toDomain() = OnBoardingContent(
        page = page,
        data = data.map { it.toDomain() }
    )
}