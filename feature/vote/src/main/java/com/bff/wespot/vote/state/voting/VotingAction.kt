package com.bff.wespot.vote.state.voting

sealed class VotingAction {
    data object StartVoting : VotingAction()
}
