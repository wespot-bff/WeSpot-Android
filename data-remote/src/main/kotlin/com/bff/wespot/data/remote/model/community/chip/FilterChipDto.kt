package com.bff.wespot.data.remote.model.community.chip

import com.bff.wespot.data.remote.model.serverDriven.type.IconTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.RichTextTypeDto
import com.bff.wespot.model.community.chip.FilterChip
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FilterChip")
data class FilterChipDto(
    val content: FilterChipContentDto
) : BaseChipDto {
    override fun toDomain() = FilterChip(
        id = content.id,
        icon = content.icon.toDomain(),
        text = content.text.toDomain(),
        target = content.target
    )
}

@Serializable
data class FilterChipContentDto(
    val id: String,
    val icon: IconTypeDto,
    val text: RichTextTypeDto,
    val target: String
)