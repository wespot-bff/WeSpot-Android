package com.bff.wespot.community.categorydetail.state

import com.bff.wespot.model.community.chip.CategoryItem

sealed interface CategoryDetailSideEffect {
    data object NavigateBack : CategoryDetailSideEffect
    data class NavigateToPostDetail(
        val postId: String,
        val navigateToComment: Boolean = false,
    ) : CategoryDetailSideEffect

    data class NavigateToCreate(val category: CategoryItem) : CategoryDetailSideEffect
}
