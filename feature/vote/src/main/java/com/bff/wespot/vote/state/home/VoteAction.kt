package com.bff.wespot.vote.state.home

sealed class VoteAction {
    data object StartDate : VoteAction()
    data object EndDate : VoteAction()
}
