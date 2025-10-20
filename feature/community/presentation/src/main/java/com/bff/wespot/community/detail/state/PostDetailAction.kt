package com.bff.wespot.community.detail.state

import com.bff.wespot.community.uimodel.PostDetailUiModel

sealed interface PostDetailAction {
    data object OnBackClick : PostDetailAction
    data class OnCategoryClick(
        val target: String,
        val categoryText: String,
    ) : PostDetailAction
    data object OnNotificationClick : PostDetailAction
    data class OnReactionClick(
        val reaction: PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) : PostDetailAction

    data object OnScrapClick : PostDetailAction
    data class OnCommentChange(
        val content: String,
    ) : PostDetailAction
    data class OnCommentSend(
        val content: String,
    ) : PostDetailAction
    data class OnCommentLike(
        val commentId: String,
    ) : PostDetailAction
    data class OnCommentReport(
        val commentId: String,
    ) : PostDetailAction
    data class OnCommentDelete(
        val commentId: String,
    ) : PostDetailAction
    data object OnDismissCommentDeleteDialog : PostDetailAction
    data object OnConfirmCommentDelete : PostDetailAction
    data object RefreshPost : PostDetailAction

    // Bottom sheet actions
    data object OnMoreOptionClicked : PostDetailAction
    data object OnDismissPostOptions : PostDetailAction
    data class OnSheetItemClicked(
        val option: PostDetailUiState.SheetItem.SheetType,
    ) : PostDetailAction

    // Dialog actions
    data object OnDismissDeleteDialog : PostDetailAction
    data object OnConfirmDelete : PostDetailAction
    data object OnDismissBlockDialog : PostDetailAction
    data object OnConfirmBlock : PostDetailAction
}
