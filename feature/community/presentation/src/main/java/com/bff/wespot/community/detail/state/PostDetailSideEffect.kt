package com.bff.wespot.community.detail.state

import com.bff.wespot.community.uimodel.PostDetailUiModel

sealed interface PostDetailSideEffect {
    data class NavigateToEditPost(
        val id: String,
        val postData: PostDetailUiModel.PostDetailContentUiModel,
    ) : PostDetailSideEffect

    data object OnBackClick : PostDetailSideEffect

    data class OnCategoryClick(val target: String, val categoryText: String) : PostDetailSideEffect

    data object NavigateToReportScreen : PostDetailSideEffect

    data object OnPostDeletedOrBlocked : PostDetailSideEffect
}
