package com.bff.wespot.entire.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.domain.usecase.UpdateProfileCharacterUseCase
import com.bff.wespot.domain.usecase.UpdateProfileIntroductionUseCase
import com.bff.wespot.entire.R
import com.bff.wespot.entire.common.INPUT_DEBOUNCE_TIME
import com.bff.wespot.entire.common.INTRODUCTION_MAX_LENGTH
import com.bff.wespot.entire.state.edit.EntireEditAction
import com.bff.wespot.entire.state.edit.EntireEditSideEffect
import com.bff.wespot.entire.state.edit.EntireEditUiState
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.user.response.ProfileCharacter
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
class EntireEditViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val commonRepository: CommonRepository,
    private val updateProfileIntroductionUseCase: UpdateProfileIntroductionUseCase,
    private val updateProfileCharacterUseCase: UpdateProfileCharacterUseCase,
    private val checkProfanityUseCase: CheckProfanityUseCase,
) : ViewModel(), ContainerHost<EntireEditUiState, EntireEditSideEffect> {
    override val container = container<EntireEditUiState, EntireEditSideEffect>(EntireEditUiState())

    private val introductionInput: MutableStateFlow<String> = MutableStateFlow("")

    fun onAction(action: EntireEditAction) {
        when (action) {
            EntireEditAction.OnCharacterEditScreenEntered -> {
                handleCharacterEditScreenEntered()
                observeProfileFlow()
            }
            EntireEditAction.OnIntroductionEditDoneButtonClicked -> updateIntroduction()
            is EntireEditAction.OnProfileEditScreenEntered -> {
                handleProfileEditScreenEntered()
                observeProfileFlow()
                observeIntroductionInput()

                if (action.isCompleteEdit) {
                    postEditDoneSideToast()
                }
            }
            is EntireEditAction.OnProfileEditTextFieldFocused ->
                handleProfileEditButtonText(action.focused)
            is EntireEditAction.OnIntroductionChanged -> handleIntroductionChanged(action.introduction)
            is EntireEditAction.OnCharacterEditDoneButtonClicked -> updateCharacter(action.character)
        }
    }

    private fun handleCharacterEditScreenEntered() = intent {
        viewModelScope.launch {
            launch {
                commonRepository.getBackgroundColors()
                    .onSuccess { backgroundColorList ->
                        reduce { state.copy(backgroundColorList = backgroundColorList) }
                    }
                    .onFailure { Timber.e(it) }
            }

            launch {
                commonRepository.getCharacters()
                    .onSuccess { characterList ->
                        reduce { state.copy(characterList = characterList) }
                    }
                    .onFailure { Timber.e(it) }
            }
        }
    }

    private fun handleProfileEditScreenEntered() {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile()
            }.onSuccess { profile ->
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
                }
                .collect {
                    reduce { state.copy(profile = it) }
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

    private fun postEditDoneSideToast() = intent {
        postSideEffect(
            EntireEditSideEffect.ShowToast(
                ToastState(
                    show = true,
                    message = R.string.edit_done,
                    type = WSToastType.Success,
                ),
            ),
        )
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

    private fun updateIntroduction() = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            updateProfileIntroductionUseCase(state.introductionInput)
                .onSuccess {
                    postEditDoneSideToast()
                    reduce { state.copy(isLoading = false) }
                }
                .onFailure {
                    Timber.e(it)
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun updateCharacter(character: ProfileCharacter) = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            updateProfileCharacterUseCase(character = character)
                .onSuccess {
                    postSideEffect(EntireEditSideEffect.NavigateToEntire)
                    reduce { state.copy(isLoading = false) }
                }
                .onFailure {
                    Timber.e(it)
                    reduce { state.copy(isLoading = false) }
                }
        }
    }
}
