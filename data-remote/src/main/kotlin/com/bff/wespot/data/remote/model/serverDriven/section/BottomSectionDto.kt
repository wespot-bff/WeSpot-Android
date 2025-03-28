package com.bff.wespot.data.remote.model.serverDriven.section

import com.bff.wespot.data.remote.model.serverDriven.BaseComponentDto
import com.bff.wespot.model.serverDriven.section.BottomSection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bottomSection")
data class BottomSectionDto(
    val components: List<BaseComponentDto>
) : BaseSectionDto {
    override fun toDomain() = BottomSection(
        components = components.map { it.toDomain() }
    )
}