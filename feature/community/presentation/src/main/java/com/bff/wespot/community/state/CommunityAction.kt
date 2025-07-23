package com.bff.wespot.community.state

sealed interface CommunityAction {
    data class OnFilterChipClicked(val id: String, val target: String) : CommunityAction
    data object OnMoreClicked : CommunityAction
}
