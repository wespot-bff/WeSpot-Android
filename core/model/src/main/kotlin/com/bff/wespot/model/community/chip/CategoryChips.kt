package com.bff.wespot.model.community.chip

data class CategoryChips(
    val category: String,
    val chips: List<CategoryItem>,
)

data class CategoryItem(
    val id: String,
    val text: String,
) {
    companion object {
        val EMPTY = CategoryItem(
            id = "",
            text = "",
        )
    }
}
