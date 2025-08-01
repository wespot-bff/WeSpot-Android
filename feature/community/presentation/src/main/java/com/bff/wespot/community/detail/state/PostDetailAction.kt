package com.bff.wespot.community.detail.state

sealed interface PostDetailAction {
    data object OnBackClick : PostDetailAction
    data object OnCategoryClick : PostDetailAction
    data object OnProfileClick : PostDetailAction
    data object OnNotificationClick : PostDetailAction
    data class OnReactionClick(val reaction: String) : PostDetailAction
    data object OnScrapClick : PostDetailAction
    data class OnCommentChange(val content: String) : PostDetailAction
    data class OnCommentSend(val content: String) : PostDetailAction
    data class OnCommentLike(val commentId: String) : PostDetailAction
    data class OnCommentReport(val commentId: String) : PostDetailAction
}
