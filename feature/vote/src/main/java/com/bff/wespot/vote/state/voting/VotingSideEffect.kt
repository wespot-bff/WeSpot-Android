package com.bff.wespot.vote.state.voting

sealed class VotingSideEffect {
    data object NavigateToResult : VotingSideEffect()
    data class ShowToast(val message: String) : VotingSideEffect()
}
