package com.bff.wespot.community.categorydetail.state

import com.bff.wespot.community.uimodel.PostItemUiModel

sealed interface CategoryDetailAction {
    data object OnBackClick : CategoryDetailAction
    data class OnReactionClick(
        val id: String,
        val reaction: PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) : CategoryDetailAction
    data class OnScrapClick(
        val id: String,
    ) : CategoryDetailAction
    data object OnRefresh : CategoryDetailAction
    data class OnCategoryChange(
        val categoryId: String,
    ) : CategoryDetailAction

    data object LoadData : CategoryDetailAction

    data object OnFABClicked : CategoryDetailAction
}
