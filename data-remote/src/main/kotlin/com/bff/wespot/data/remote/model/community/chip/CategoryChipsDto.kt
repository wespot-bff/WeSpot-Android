package com.bff.wespot.data.remote.model.community.chip

import com.bff.wespot.model.community.chip.CategoryChips
import com.bff.wespot.model.community.chip.CategoryItem
import kotlinx.serialization.Serializable

@Serializable
data class CategoryChipsDto(
    val category: String,
    val chips: List<CategoryItemDto>,
) {
    fun toDomain() = CategoryChips(
        category = category,
        chips = chips.map { it.toDomain() }
    )
}

@Serializable
data class CategoryItemDto(
    val id: String,
    val text: String,
) {
    fun toDomain() = CategoryItem(
        id = id,
        text = text,
    )
}