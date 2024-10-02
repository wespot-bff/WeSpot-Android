package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.model.common.ReportType
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.vote.request.VoteResultUpload
import com.bff.wespot.model.vote.request.VoteResultsUpload
import com.bff.wespot.model.vote.response.VoteItem
import com.bff.wespot.vote.state.voting.VotingAction
import com.bff.wespot.vote.state.voting.VotingSideEffect
import com.bff.wespot.vote.state.voting.VotingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VotingViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val commonRepository: CommonRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(), ContainerHost<VotingUiState, VotingSideEffect> {
    override val container = container<VotingUiState, VotingSideEffect>(
        VotingUiState(
            playStoreLink = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.PLAY_STORE_URL),
            addQuestionLink =
                remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.VOTE_QUESTION_GOOGLE_FORM_URL),
        ),
    )

    fun onAction(action: VotingAction) {
        when (action) {
            VotingAction.StartVoting -> startVoting()
            VotingAction.GoBackVote -> goBackVote()
            is VotingAction.GoToNextVote -> goToNextVote(action.optionId)
            is VotingAction.SubmitVoteResult -> submitVoteResult()
            is VotingAction.SendReport -> sendReport(action.userId)
        }
    }

    private fun startVoting() = intent {
        reduce {
            state.copy(
                loading = true,
                start = true,
                selectedVote = emptyList(),
                voteItems = emptyList(),
            )
        }
        viewModelScope.launch(coroutineDispatcher) {
            try {
                voteRepository.getVoteQuestions()
                    .onSuccess {
                        val profile = userRepository.getProfile().getOrDefault(Profile())
                        reduce {
                            state.copy(
                                profile = profile,
                            )
                        }
                        if (it.voteItems.isEmpty()) {
                            postSideEffect(VotingSideEffect.ShowToast("투표 항목이 없습니다"))
                            reduce {
                                state.copy(loading = false)
                            }
                            return@onSuccess
                        }
                        reduce {
                            state.copy(
                                voteItems = it.voteItems,
                                totalPage = it.voteItems.size,
                                pageNumber = 1,
                                currentVote = it.voteItems.first(),
                                start = false,
                                selectedVote = List(it.voteItems.size) { VoteResultUpload() },
                                loading = false,
                            )
                        }
                    }
                    .onNetworkFailure {
                        postSideEffect(it.toSideEffect())
                    }
                    .onFailure {
                        Timber.e(it)
                        reduce {
                            state.copy(loading = false, voteItems = emptyList())
                        }
                    }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun goToNextVote(optionId: Int) = intent {
        if (state.pageNumber == state.totalPage) {
            reduce {
                state.copy(
                    selectedVote = state.selectedVote.toMutableList().apply {
                        this[state.pageNumber - 1] =
                            VoteResultUpload(state.currentVote.voteUser.id, optionId)
                    },
                )
            }
            return@intent
        }

        reduce {
            state.copy(
                pageNumber = state.pageNumber + 1,
                currentVote = state.voteItems[state.pageNumber],
                selectedVote = state.selectedVote.toMutableList().apply {
                    this[state.pageNumber - 1] =
                        VoteResultUpload(state.currentVote.voteUser.id, optionId)
                },
            )
        }
    }

    private fun goBackVote() = intent {
        if (state.pageNumber == 1 || state.voteItems.isEmpty()) {
            return@intent
        }

        reduce {
            state.copy(
                pageNumber = state.pageNumber - 1,
                currentVote = state.voteItems[state.pageNumber - 2],
                selectedVote = state.selectedVote.toMutableList().apply {
                    this[state.pageNumber - 1] = VoteResultUpload()
                },
            )
        }
    }

    private fun submitVoteResult() = intent {
        reduce {
            state.copy(loading = true)
        }
        viewModelScope.launch(coroutineDispatcher) {
            val result = voteRepository.uploadVoteResults(VoteResultsUpload(state.selectedVote))

            if (result) {
                postSideEffect(VotingSideEffect.NavigateToResult)
                dispose()
            } else {
                postSideEffect(VotingSideEffect.ShowToast("투표 제출에 오류가 발생했습니다"))
            }
            reduce {
                state.copy(loading = false)
            }
        }
    }

    private fun sendReport(userId: Int) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            commonRepository.sendReport(ReportType.VOTE, userId)
                .onSuccess {
                    postSideEffect(VotingSideEffect.ShowToast("제보 접수 완료"))
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
        }
    }

    private fun dispose() = intent {
        reduce {
            state.copy(
                voteItems = emptyList(),
                pageNumber = -1,
                totalPage = 0,
                currentVote = VoteItem(),
                selectedVote = emptyList(),
                start = true,
                loading = false,
            )
        }
    }
}
