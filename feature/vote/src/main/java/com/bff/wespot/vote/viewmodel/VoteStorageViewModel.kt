package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.vote.response.ReceivedVoteData
import com.bff.wespot.model.vote.response.SentVoteData
import com.bff.wespot.vote.state.storage.StorageAction
import com.bff.wespot.vote.state.storage.StorageSideEffect
import com.bff.wespot.vote.state.storage.StorageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class VoteStorageViewModel @Inject constructor(
    private val receivedRepository: BasePagingRepository<ReceivedVoteData, Paging<ReceivedVoteData>>,
    private val sentRepository: BasePagingRepository<SentVoteData, Paging<SentVoteData>>,
) : BaseViewModel(), ContainerHost<StorageUiState, StorageSideEffect> {
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
        reduce {
            state.copy(isLoading = true)
        }

        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        receivedVotes = receivedRepository.fetchResultStream()
                            .cachedIn(viewModelScope),
                        isLoading = false,
                    )
                }
            }.onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    private fun getSentVotes() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        sentVotes = sentRepository.fetchResultStream().cachedIn(viewModelScope),
                        isLoading = false,
                    )
                }
            }.onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    private fun toIndividualVote(optionId: Int, date: String, isReceived: Boolean) = intent {
        postSideEffect(StorageSideEffect.NavigateToIndividualVote(optionId, date, isReceived))
    }
}
