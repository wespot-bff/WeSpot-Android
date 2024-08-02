package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.vote.state.storage.StorageAction
import com.bff.wespot.vote.state.storage.StorageSideEffect
import com.bff.wespot.vote.state.storage.StorageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VoteStorageViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel(), ContainerHost<StorageUiState, StorageSideEffect> {
    override val container = container<StorageUiState, StorageSideEffect>(StorageUiState())

    fun onAction(action: StorageAction) {
        when (action) {
            is StorageAction.GetReceivedVotes -> getReceivedVotes()
            is StorageAction.GetSentVotes -> getSentVotes()
            is StorageAction.ToIndividualVote -> toIndividualVote(
                action.optionId,
                action.date,
                action.isReceived,
            )
        }
    }

    private fun getReceivedVotes() = intent {
        val original = state.receivedVotes

        reduce {
            state.copy(isLoading = true, receivedVotes = emptyList())
        }

        viewModelScope.launch(coroutineDispatcher) {
            voteRepository.getVoteReceived()
                .onSuccess {
                    reduce {
                        state.copy(
                            receivedVotes = it.voteData,
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    Timber.e(it)
                    reduce {
                        state.copy(
                            receivedVotes = original,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun getSentVotes() = intent {
        val original = state.sentVotes

        reduce {
            state.copy(isLoading = true, sentVotes = emptyList())
        }

        viewModelScope.launch(coroutineDispatcher) {
            voteRepository.getVoteSent()
                .onSuccess {
                    reduce {
                        state.copy(
                            sentVotes = it.voteData,
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    Timber.e(it)
                    reduce {
                        state.copy(
                            sentVotes = original,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun toIndividualVote(optionId: Int, date: String, isReceived: Boolean) = intent {
        postSideEffect(StorageSideEffect.NavigateToIndividualVote(optionId, date, isReceived))
    }
}
