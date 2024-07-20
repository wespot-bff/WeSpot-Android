package com.bff.wespot.vote.state

sealed class VoteAction {
    data object StartDate : VoteAction()
    data object EndDate : VoteAction()
}
