package com.bff.wespot.community.uimodel.chip

import com.bff.wespot.model.community.chip.FilterChip
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class FilterChipUiModel(
    override val id: String,
    val icon: IconType,
    val text: RichTextType,
    val target: String,
) : BaseChipUiModel

fun FilterChip.toUiModel() = FilterChipUiModel(
    id = id,
    icon = icon,
    text = text,
    target = target,
)
