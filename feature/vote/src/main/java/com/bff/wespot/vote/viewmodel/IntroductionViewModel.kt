package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.vote.state.profile.ProfileAction
import com.bff.wespot.vote.state.profile.ProfileSideEffect
import com.bff.wespot.vote.state.profile.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val commonRepository: CommonRepository,
    private val dispatcher: CoroutineDispatcher,
    private val checkProfanityUseCase: CheckProfanityUseCase,
) : ViewModel(), ContainerHost<ProfileUiState, ProfileSideEffect> {
    override val container: Container<ProfileUiState, ProfileSideEffect> =
        container(
            ProfileUiState(
                backgroundColor = savedStateHandle["backgroundColor"] ?: "",
                iconUrl = savedStateHandle["iconUrl"] ?: "",
            ),
        )

    private val userInput = MutableStateFlow("")

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.UpdateIntroduction -> updateIntroduction(action.introduction)
            is ProfileAction.StartIntroduction -> startIntroduction()
            is ProfileAction.EditProfile -> editProfile()
        }
    }

    private fun updateIntroduction(introduction: String) = intent {
        reduce {
            if (introduction.length > 20) {
                return@reduce state
            }

            userInput.value = introduction
            state.copy(
                introduction = introduction,
            )
        }
    }

    private fun startIntroduction() = intent {
        viewModelScope.launch(dispatcher) {
            userInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect {
                    runCatching {
                        val result = checkProfanityUseCase(it)
                        reduce {
                            state.copy(
                                hasProfanity = result,
                            )
                        }
                    }
                }
        }
    }

    private fun editProfile() = intent {
        viewModelScope.launch(dispatcher) {
            commonRepository.EditProfile(
                introduction = state.introduction,
                backgroundColor = state.backgroundColor,
                iconUrl = state.iconUrl,
            ).onSuccess {
                postSideEffect(ProfileSideEffect.NavigateToVoteHome)
            }
        }
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}
