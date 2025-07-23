package com.bff.wespot.community.write.state

import com.bff.wespot.model.community.chip.CategoryChips
import com.bff.wespot.model.community.chip.CategoryItem

data class WritePostUiState(
    val selectedCategory: CategoryItem = CategoryItem.EMPTY,
    val title: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val categories: List<CategoryChips> = emptyList(),
)
