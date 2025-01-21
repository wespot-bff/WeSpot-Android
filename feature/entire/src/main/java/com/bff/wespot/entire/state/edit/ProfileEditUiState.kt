package com.bff.wespot.entire.state.edit

import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.Character
import com.bff.wespot.model.user.response.Profile

data class ProfileEditUiState(
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
    val changeBottomSheet: Boolean = false,
) {
    fun isEditedProfile() = profile.introduction != introductionInput || profilePath != profile.profileCharacter.iconUrl

    fun isValidIntroduce() = hasProfanity.not() && introductionInput.length in 0..20
}
