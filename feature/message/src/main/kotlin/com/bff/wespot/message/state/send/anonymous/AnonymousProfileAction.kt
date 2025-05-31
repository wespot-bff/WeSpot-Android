package com.bff.wespot.message.state.send.anonymous

import com.bff.wespot.message.model.AnonymousProfile

sealed interface AnonymousProfileAction {
    data class OnProfileModalOpened(val profile: AnonymousProfile) : AnonymousProfileAction
    data object OnProfileImageClicked : AnonymousProfileAction
    data class OnProfileNameChanged(val name: String) : AnonymousProfileAction
    data class OnProfileImagePicked(val imagePath: String) : AnonymousProfileAction
    data object OnPickerOpenOptionClicked : AnonymousProfileAction
    data object OnRemoveProfileOptionClicked : AnonymousProfileAction
    data object OnProfileOptionSheetClosed : AnonymousProfileAction
}
