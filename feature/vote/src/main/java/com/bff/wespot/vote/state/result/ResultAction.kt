package com.bff.wespot.vote.state.result

sealed class ResultAction {
    data class LoadVoteResults(val date: String) : ResultAction()
}
