package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.common.KakaoSharingType
import com.bff.wespot.model.vote.response.VoteResults
import com.bff.wespot.vote.state.result.ResultAction
import com.bff.wespot.vote.state.result.ResultSideEffect
import com.bff.wespot.vote.state.result.ResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VoteResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val voteRepository: VoteRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val commonRepository: CommonRepository,
) : ViewModel(), ContainerHost<ResultUiState, ResultSideEffect> {
    override val container = container<ResultUiState, ResultSideEffect>(
        ResultUiState(
            isVoting = savedStateHandle["isVoting"] ?: false,
        ),
    )

    fun onAction(action: ResultAction) {
        when (action) {
            is ResultAction.LoadVoteResults -> loadVoteResults(action.date)
            is ResultAction.GetOnBoarding -> getOnBoarding()
            is ResultAction.SetVoteOnBoarding -> setVoteOnBoarding()
            is ResultAction.GetKakaoContent -> getKakaoContent()
        }
    }

    private fun loadVoteResults(date: String) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            val previous = state.voteResults
            reduce { state.copy(isLoading = true, voteResults = VoteResults(emptyList())) }
            try {
                voteRepository.getVoteResults(date)
                    .onSuccess {
                        reduce { state.copy(voteResults = it, isLoading = false) }
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false, voteResults = previous) }
                        Timber.e(it)
                    }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getOnBoarding() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            dataStoreRepository.getBoolean(DataStoreKey.VOTE_ONBOARDING).collect {
                if (!it) {
                    reduce { state.copy(onBoarding = !it) }
                }
            }
        }
    }

    private fun setVoteOnBoarding() = intent {
        reduce {
            viewModelScope.launch {
                dataStoreRepository.saveBoolean(DataStoreKey.VOTE_ONBOARDING, true)
            }
            state.copy(
                onBoarding = false,
            )
        }
    }

    private fun getKakaoContent() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            commonRepository.getKakaoContent(KakaoSharingType.TELL.name)
                .onSuccess {
                    reduce { state.copy(kakaoContent = it) }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
