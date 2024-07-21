package com.bff.wespot.vote.state.voting

sealed class VotingAction {
    data object StartVoting : VotingAction()
    data class GoToNextVote(val optionId: Int): VotingAction()
    data object GoBackVote : VotingAction()
}
