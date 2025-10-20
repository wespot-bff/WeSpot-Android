package com.bff.wespot.community.state

import com.bff.wespot.community.uimodel.PostItemUiModel

sealed interface CommunityAction {
    data class OnFilterChipClicked(
        val id: String,
        val target: String,
    ) : CommunityAction
    data object OnMoreClicked : CommunityAction
    data object OnWritePostClicked : CommunityAction
    data object OnCommunityEnter : CommunityAction
    data class OnReactionClick(
        val id: String,
        val reaction: PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) : CommunityAction
    data class OnScrapClick(
        val id: String,
        val isCurrentlyScrapped: Boolean,
    ) : CommunityAction
    data object OnRefresh : CommunityAction
}
