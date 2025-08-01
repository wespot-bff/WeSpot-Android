package com.bff.wespot.community.detail.state

sealed interface PostDetailAction {
    data object OnBackClick : PostDetailAction
    data object OnCategoryClick : PostDetailAction
    data object OnProfileClick : PostDetailAction
    data object OnNotificationClick : PostDetailAction
    data class OnReactionClick(val reaction: String) : PostDetailAction
    data object OnScrapClick : PostDetailAction
}
