package com.bff.wespot.vote.state.profile

sealed class ProfileSideEffect {
    data object NavigateToVoteHome : ProfileSideEffect()
}
