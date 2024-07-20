package com.danggeun.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danggeun.common.util.toDateTimeString
import com.danggeun.vote.state.VoteAction
import com.danggeun.vote.state.VoteSideEffect
import com.danggeun.vote.state.VoteUiState
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
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
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
}
