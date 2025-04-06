package com.bff.wespot.data.remote.model.serverDriven.section

import com.bff.wespot.data.remote.model.serverDriven.BaseComponentDto
import com.bff.wespot.model.serverDriven.section.ContentSection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("contentSection")
data class ContentSectionDto(
    val components: List<BaseComponentDto>
) : BaseSectionDto {
    override fun toDomain() = ContentSection(
        components = components.map { it.toDomain() }
    )
}