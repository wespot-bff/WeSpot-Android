package com.bff.wespot.community.search.state

sealed interface SearchAction {
    data class HandleSearchChange(val keyword: String) : SearchAction
    data class NavigateToDetail(val postId: String) : SearchAction
    data object MonitorUserInput : SearchAction
}
