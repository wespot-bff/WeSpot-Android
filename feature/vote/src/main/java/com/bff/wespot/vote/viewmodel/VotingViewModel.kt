package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.vote.state.voting.VotingAction
import com.bff.wespot.vote.state.voting.VotingSideEffect
import com.bff.wespot.vote.state.voting.VotingUiState
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
class VotingViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel(), ContainerHost<VotingUiState, VotingSideEffect> {
    override val container = container<VotingUiState, VotingSideEffect>(VotingUiState())

    fun onAction(action: VotingAction) {
        when (action) {
            VotingAction.StartVoting -> startVoting()
        }
    }

    private fun startVoting() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            voteRepository.getVoteQuestions()
                .onSuccess {
                    reduce {
                        state.copy(voteItems = it)
                    }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
