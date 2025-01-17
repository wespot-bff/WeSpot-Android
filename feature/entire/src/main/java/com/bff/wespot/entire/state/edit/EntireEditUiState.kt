package com.bff.wespot.entire.state.edit

import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.Character
import com.bff.wespot.model.user.response.Profile

data class EntireEditUiState(
    val profile: Profile = Profile(),
    val introductionInput: String = "",
    val hasProfanity: Boolean = false,
    val isIntroductionEditing: Boolean = false,
    val backgroundColorList: List<BackgroundColor> = listOf(),
    val characterList: List<Character> = listOf(),
    val isLoading: Boolean = false,
    val profileChangeGoogleFormUrl: String = "",
    val requestDialog: Boolean = false,
    val profilePath: String? = null,
    val loading: Boolean = false,
    val changeBottomSheet: Boolean = false,
) {
    fun isValidIntroduceInputChanged(): Boolean =
        profile.introduction != introductionInput && introductionInput.length in 0..20

    fun isProfileImageChange(): Boolean =
        profilePath != profile.profileCharacter.iconUrl
}
