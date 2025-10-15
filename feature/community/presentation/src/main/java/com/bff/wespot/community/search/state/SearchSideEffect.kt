package com.bff.wespot.community.search.state

sealed interface SearchSideEffect {
    data class NavigateToDetail(val postId: String) : SearchSideEffect
}
