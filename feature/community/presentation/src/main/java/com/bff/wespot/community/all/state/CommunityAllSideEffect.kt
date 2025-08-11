package com.bff.wespot.community.all.state

sealed interface CommunityAllSideEffect {
    data class NavigateToDetail(
        val id: String,
        val navigateToComment: Boolean = false,
    ) : CommunityAllSideEffect

    data class NavigateToCategory(
        val categoryId: String,
        val categoryText: String,
    ) : CommunityAllSideEffect

    data object NavigateUp : CommunityAllSideEffect
}
