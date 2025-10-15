package com.bff.wespot.community.detail.state

import com.bff.wespot.community.uimodel.PostCommentUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel

data class PostDetailUiState(
    val detail: PostDetailUiModel = PostDetailUiModel.Empty,
    val comments: List<PostCommentUiModel> = emptyList(),
    val isLiked: Boolean = false,
    val isScrapped: Boolean = false,
    val registered: Boolean = false,
    val commentInput: String = "",
)
