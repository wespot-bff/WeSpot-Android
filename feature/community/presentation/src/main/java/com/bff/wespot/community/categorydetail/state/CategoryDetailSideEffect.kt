package com.bff.wespot.community.categorydetail.state

sealed interface CategoryDetailSideEffect {
    data object NavigateBack : CategoryDetailSideEffect
    data class NavigateToPostDetail(val postId: String) : CategoryDetailSideEffect
}
