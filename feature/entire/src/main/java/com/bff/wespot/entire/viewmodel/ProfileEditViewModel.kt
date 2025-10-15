package com.bff.wespot.entire.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.domain.util.RemoteConfigKey.PROFILE_CHANGE_GOOGLE_FORM_URL
import com.bff.wespot.entire.R
import com.bff.wespot.entire.common.INPUT_DEBOUNCE_TIME
import com.bff.wespot.entire.common.INTRODUCTION_MAX_LENGTH
import com.bff.wespot.entire.state.edit.ProfileEditAction
import com.bff.wespot.entire.state.edit.ProfileEditSideEffect
import com.bff.wespot.entire.state.edit.ProfileEditUiState
import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.ui.model.ToastState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val commonRepository: CommonRepository,
    private val checkProfanityUseCase: CheckProfanityUseCase,
    remoteConfigRepository: RemoteConfigRepository,
) : BaseViewModel(),
    ContainerHost<ProfileEditUiState, ProfileEditSideEffect> {
    override val container = container<ProfileEditUiState, ProfileEditSideEffect>(
        ProfileEditUiState(
            profileChangeGoogleFormUrl =
                remoteConfigRepository.fetchFromRemoteConfig(PROFILE_CHANGE_GOOGLE_FORM_URL),
        ),
    )

    private val introductionInput: MutableStateFlow<String> = MutableStateFlow("")

    fun onAction(action: ProfileEditAction) = intent {
        when (action) {
            ProfileEditAction.OnProfileEditDoneButtonClicked -> {
                handleProfileUpdate()
            }

            is ProfileEditAction.OnProfileEditScreenEntered -> {
                cacheUserProfile()
                observeProfileFlow()
                observeIntroductionInput()
            }

            is ProfileEditAction.OnProfileEditTextFieldFocused ->
                handleProfileEditButtonText(action.focused)

            is ProfileEditAction.OnIntroductionChanged -> handleIntroductionChanged(action.introduction)
            is ProfileEditAction.OnRequestDialogDismissed -> {
                reduce { state.copy(requestDialog = false) }
            }

            is ProfileEditAction.OnRequestDialogShown -> {
                reduce { state.copy(requestDialog = true) }
            }

            is ProfileEditAction.OnProfileImagePicked -> {
                reduce { state.copy(profilePath = action.profilePath) }
            }

            is ProfileEditAction.ChangeBottomSheetState -> {
                reduce { state.copy(changeBottomSheet = action.isBottomSheetOpen) }
            }

            is ProfileEditAction.OpenPicker -> {
                postSideEffect(ProfileEditSideEffect.OpenPicker)
            }
        }
    }

    private fun cacheUserProfile() {
        viewModelScope.launch {
            runCatching {
                val profile = profileRepository.getProfile()
                handleIntroductionChanged(profile.introduction)
            }
        }
    }

    private fun observeProfileFlow() = intent {
        viewModelScope.launch {
            profileRepository.profileDataFlow
                .distinctUntilChanged()
                .catch { exception ->
                    Timber.e(exception)
                }.collect {
                    reduce { state.copy(profile = it, profilePath = it.profileCharacter.iconUrl) }
                }
        }
    }

    private fun handleIntroductionChanged(introduction: String) = intent {
        reduce {
            introductionInput.value = introduction
            state.copy(introductionInput = introduction)
        }
    }

    private fun handleProfileEditButtonText(focused: Boolean) = intent {
        reduce {
            state.copy(isIntroductionEditing = focused)
        }
    }

    private fun observeIntroductionInput() = intent {
        viewModelScope.launch {
            introductionInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect { introduction ->
                    if (introduction.length in 1..INTRODUCTION_MAX_LENGTH &&
                        introduction != state.profile.introduction
                    ) {
                        hasProfanity(introduction)
                    }
                }
        }
    }

    private fun hasProfanity(introduction: String) = intent {
        runCatching {
            val result = checkProfanityUseCase(introduction)
            reduce {
                state.copy(
                    hasProfanity = result,
                )
            }
        }
    }

    private fun handleProfileUpdate() = intent {
        viewModelScope.launch {
            reduce { state.copy(isLoading = true) }
            val profilePath = state.profilePath
            if (profilePath == null) {
                updateProfile(null)
                return@launch
            }

            uploadProfileImage(profilePath)
                .onSuccess {
                    updateProfile(it)
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.onFailure {
                    reduce { state.copy(isLoading = false) }
                    Timber.e(it)
                }
        }
    }

    private suspend fun uploadProfileImage(profilePath: String): Result<String> =
        runCatching {
            commonRepository.uploadImage(profilePath)
        }.mapCatching { uploadResult ->
            if (!uploadResult.isSuccess) throw NetworkException()
            uploadResult.getOrThrow()
        }

    private suspend fun updateProfile(url: String?) = intent {
        runCatching {
            profileRepository.updateProfile(
                introduction = state.introductionInput,
                profileImageUrl = url,
            )
        }.onSuccess {
            profileRepository.setProfile(
                state.profile.copy(
                    profileCharacter = state.profile.profileCharacter.copy(
                        iconUrl = state.profilePath ?: "",
                    ),
                    introduction = state.introductionInput,
                ),
            )
            postSideEffect(
                ProfileEditSideEffect.ShowToast(
                    ToastState(
                        show = true,
                        message = R.string.edit_done,
                        type = WSToastType.Success,
                    ),
                ),
            )
        }.onFailure {
            Timber.e(it)
        }.also {
            reduce { state.copy(isLoading = false) }
        }
    }
}
