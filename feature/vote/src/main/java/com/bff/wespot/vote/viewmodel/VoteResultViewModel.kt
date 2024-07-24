package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bff.wespot.vote.state.result.ResultSideEffect
import com.bff.wespot.vote.state.result.ResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class VoteResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<ResultUiState, ResultSideEffect> {
    override val container = container<ResultUiState, ResultSideEffect>(ResultUiState())
}
