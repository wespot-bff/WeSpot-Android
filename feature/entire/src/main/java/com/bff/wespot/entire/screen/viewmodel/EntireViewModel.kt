package com.bff.wespot.entire.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.entire.screen.common.INPUT_DEBOUNCE_TIME
import com.bff.wespot.entire.screen.common.INTRODUCTION_MAX_LENGTH
import com.bff.wespot.entire.screen.state.EntireAction
import com.bff.wespot.entire.screen.state.EntireSideEffect
import com.bff.wespot.entire.screen.state.EntireUiState
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EntireViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val checkProfanityUseCase: CheckProfanityUseCase,
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
) : ViewModel(), ContainerHost<EntireUiState, EntireSideEffect> {
    override val container = container<EntireUiState, EntireSideEffect>(EntireUiState())

    private val introductionInput: MutableStateFlow<String> = MutableStateFlow("")

    fun onAction(action: EntireAction) {
        when (action) {
            EntireAction.OnEntireScreenEntered, EntireAction.OnRevokeScreenEntered -> getProfile()
            EntireAction.OnProfileEditScreenEntered -> {
                getProfile()
                observeIntroductionInput()
            }
            EntireAction.OnRevokeButtonClicked -> revokeUser()
            EntireAction.OnSignOutButtonClicked -> signOut()
            EntireAction.OnRevokeConfirmed -> handleRevokeConfirmed()
            EntireAction.OnIntroductionEditDoneButtonClicked -> postIntroduction()
            EntireAction.OnBlockListScreenEntered -> getUnBlockedMessage()
            EntireAction.UnBlockMessage -> unblockMessage()
            is EntireAction.OnProfileEditTextFieldFocused ->
                handleProfileEditButtonText(action.focused)
            is EntireAction.OnUnBlockButtonClicked -> handleUnBlockButtonClicked(action.messageId)
            is EntireAction.OnRevokeReasonSelected -> handleRevokeReasonSelected(action.reason)
            is EntireAction.OnIntroductionChanged -> handleIntroductionChanged(action.introduction)
        }
    }

    private fun getProfile() = intent {
        viewModelScope.launch {
            userRepository.getProfile()
                .onSuccess { profile ->
                    reduce { state.copy(profile = profile) }
                    handleIntroductionChanged(profile.introduction)
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun revokeUser() = intent {
        viewModelScope.launch {
            // TODO Token 삭제
            authRepository.revoke(state.revokeReasonList)
                .onSuccess {
                    postSideEffect(EntireSideEffect.NavigateToAuth)
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun signOut() = intent {
        viewModelScope.launch {
            // TODO Token 삭제
            postSideEffect(EntireSideEffect.NavigateToAuth)
        }
    }

    private fun getUnBlockedMessage() = intent {
        viewModelScope.launch {
            messageRepository.getBlockedMessage(cursorId = 0) // TODO Cursor Paging
                .onSuccess { blockedMessageList ->
                    reduce { state.copy(blockedMessageList = blockedMessageList) }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleUnBlockButtonClicked(messageId: Int) = intent {
        reduce { state.copy(unBlockMessageId = messageId) }
    }

    private fun unblockMessage() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageStorageRepository.blockMessage(state.unBlockMessageId)
                .onSuccess {
                    if (state.unBlockList.contains(state.unBlockMessageId).not()) {
                        val updatedList = state.unBlockList.toMutableList().apply {
                            add(state.unBlockMessageId)
                        }
                        reduce { state.copy(unBlockList = updatedList, isLoading = false) }
                    }
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                    Timber.e(it)
                }
        }
    }

    private fun handleRevokeReasonSelected(reason: String) = intent {
        reduce {
            val updatedList = state.revokeReasonList.toMutableList().apply {
                if (contains(reason)) {
                    remove(reason)
                } else {
                    add(reason)
                }
            }
            state.copy(revokeReasonList = updatedList)
        }
    }

    private fun handleProfileEditButtonText(focused: Boolean) = intent {
        reduce {
            state.copy(isIntroductionEditing = focused)
        }
    }

    private fun handleRevokeConfirmed() = intent {
        reduce {
            state.copy(revokeConfirmed = state.revokeConfirmed.not())
        }
    }

    private fun handleIntroductionChanged(introduction: String) = intent {
        reduce {
            introductionInput.value = introduction
            state.copy(introductionInput = introduction)
        }
    }

    private fun observeIntroductionInput() {
        viewModelScope.launch {
            introductionInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect { introduction ->
                    if (introduction.length in 1..INTRODUCTION_MAX_LENGTH) {
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

    private fun postIntroduction() = intent {
        viewModelScope.launch {
            userRepository.updateIntroduction(state.introductionInput)
                .onSuccess {
                    postSideEffect(EntireSideEffect.ShowToast("수정 완료"))
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
