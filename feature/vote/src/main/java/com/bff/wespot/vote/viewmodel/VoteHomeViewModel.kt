package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.util.toDateTimeString
import com.bff.wespot.domain.repository.vote.VoteRepository
import com.bff.wespot.vote.state.home.VoteAction
import com.bff.wespot.vote.state.home.VoteSideEffect
import com.bff.wespot.vote.state.home.VoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VoteHomeViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val voteRepository: VoteRepository,
) : ViewModel(), ContainerHost<VoteUiState, VoteSideEffect> {
    override val container = container<VoteUiState, VoteSideEffect>(VoteUiState())

    private val _currentDate = MutableStateFlow(LocalDate.now().toDateTimeString())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    private val dateJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(dispatcher) {
            var previousDate = LocalDate.now().toDateTimeString()
            while (isActive) {
                val currentDate = LocalDate.now().toDateTimeString()
                if (currentDate != previousDate) {
                    previousDate = currentDate
                    _currentDate.value = currentDate
                }
                delay(120_000L)
            }
        }
    }

    fun onAction(action: VoteAction) {
        when (action) {
            is VoteAction.StartDate -> startUpdatingDate()
            is VoteAction.EndDate -> stopUpdatingDate()
            is VoteAction.GetFirst -> getFirstVoteResults(action.date)
            is VoteAction.OnTabChanged -> onTabChanged(action.index)
        }
    }

    private fun startUpdatingDate() {
        if (!dateJob.isActive) {
            dateJob.start()
        }
    }

    private fun stopUpdatingDate() {
        dateJob.cancel()
    }

    private fun getFirstVoteResults(date: String) = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                voteRepository.getFirstVoteResults(date)
                    .onSuccess {
                        reduce { state.copy(voteResults = it.voteResults, isLoading = false) }
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false) }
                        Timber.e(it)
                    }
            } catch (e: Exception) {
                reduce { state.copy(isLoading = false) }
                Timber.e(e)
            }
        }
    }

    private fun onTabChanged(index: Int) = intent {
        reduce { state.copy(selectedTabIndex = index) }
    }
}
