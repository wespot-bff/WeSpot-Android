package com.bff.wespot.community.all.state

sealed interface CommunityAllSideEffect {
    data class NavigateToDetail(val id: String) : CommunityAllSideEffect
    data object NavigateUp : CommunityAllSideEffect
}
