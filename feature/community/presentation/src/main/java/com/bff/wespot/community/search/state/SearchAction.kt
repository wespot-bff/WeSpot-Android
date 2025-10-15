package com.bff.wespot.community.search.state

import com.bff.wespot.community.uimodel.PostItemUiModel

sealed interface SearchAction {
    data class HandleSearchChange(val keyword: String) : SearchAction
    data class NavigateToDetail(val postId: String) : SearchAction
    data object MonitorUserInput : SearchAction
    data class OnReactionClick(
        val id: String,
        val reaction: PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) : SearchAction
    data class OnScrapClick(
        val id: String,
    ) : SearchAction
}
