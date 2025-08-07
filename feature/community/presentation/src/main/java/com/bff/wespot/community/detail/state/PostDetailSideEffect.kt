package com.bff.wespot.community.detail.state

import com.bff.wespot.community.uimodel.PostDetailUiModel

sealed interface PostDetailSideEffect {
    data class NavigateToEditPost(
        val id: String,
        val postData: PostDetailUiModel.PostDetailContentUiModel,
    ) : PostDetailSideEffect
}
