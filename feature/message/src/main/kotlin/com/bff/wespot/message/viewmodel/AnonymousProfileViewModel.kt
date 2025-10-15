package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.message.model.AnonymousProfile
import com.bff.wespot.message.state.send.anonymous.AnonymousProfileAction
import com.bff.wespot.message.state.send.anonymous.AnonymousProfileSideEffect
import com.bff.wespot.message.state.send.anonymous.AnonymousProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AnonymousProfileViewModel @Inject constructor(
    private val checkProfanityUseCase: CheckProfanityUseCase,
) : ViewModel(),
    ContainerHost<AnonymousProfileUiState, AnonymousProfileSideEffect> {
    override val container =
        container<AnonymousProfileUiState, AnonymousProfileSideEffect>(AnonymousProfileUiState())

    private val nameInput: MutableStateFlow<String> = MutableStateFlow("")

    fun onAction(action: AnonymousProfileAction) {
        when (action) {
            /** 프로필 선택 모달 */
            is AnonymousProfileAction.OnProfileModalOpened -> {
                observeNameInput()
                handleModalOpened(action.profile)
            }
            AnonymousProfileAction.OnProfileImageClicked -> handleProfileImageClicked()
            AnonymousProfileAction.OnPickerOpenOptionClicked -> handlePickerOpenOptionClicked()
            AnonymousProfileAction.OnRemoveProfileOptionClicked -> handleRemoveProfileOptionClicked()
            AnonymousProfileAction.OnProfileOptionSheetClosed -> handleProfileOptionSheetClosed()
            is AnonymousProfileAction.OnProfileNameChanged -> handleProfileNameChanged(action.name)
            is AnonymousProfileAction.OnProfileImagePicked -> handleProfileImagePicked(action.imagePath)
        }
    }

    private fun handleModalOpened(profile: AnonymousProfile) = intent {
        reduce {
            state.copy(
                name = profile.name,
                imageUrl = profile.imageUrl,
            )
        }
    }

    private fun observeNameInput() {
        viewModelScope.launch {
            nameInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect { name ->
                    if (name.length in 1..10) {
                        haseNameProfanity(name)
                    }
                }
        }
    }

    private fun haseNameProfanity(content: String) = intent {
        viewModelScope.launch {
            runCatching {
                val hasProfanity = checkProfanityUseCase(content)
                reduce {
                    state.copy(
                        hasNameProfanity = hasProfanity,
                    )
                }
            }
        }
    }

    private fun handleProfileImageClicked() = intent {
        postSideEffect(AnonymousProfileSideEffect.ShowProfileOptionBottomSheet)
    }

    private fun handleProfileImagePicked(imagePath: String) = intent {
        reduce {
            state.copy(imageUrl = imagePath)
        }
    }

    private fun handlePickerOpenOptionClicked() = intent {
        postSideEffect(AnonymousProfileSideEffect.DismissProfileOptionBottomSheet)
        postSideEffect(AnonymousProfileSideEffect.OpenPicker)
    }

    private fun handleRemoveProfileOptionClicked() = intent {
        postSideEffect(AnonymousProfileSideEffect.DismissProfileOptionBottomSheet)
        reduce {
            state.copy(imageUrl = "")
        }
    }

    private fun handleProfileOptionSheetClosed() = intent {
        postSideEffect(AnonymousProfileSideEffect.DismissProfileOptionBottomSheet)
    }

    private fun handleProfileNameChanged(name: String) = intent {
        nameInput.value = name
        reduce {
            state.copy(name = name)
        }
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}
