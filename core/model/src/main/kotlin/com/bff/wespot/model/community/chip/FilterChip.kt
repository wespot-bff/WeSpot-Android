package com.bff.wespot.model.community.chip

import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class FilterChip(
    val id: String,
    val icon: IconType,
    val text: RichTextType,
    val target: String,
) : BaseChip
