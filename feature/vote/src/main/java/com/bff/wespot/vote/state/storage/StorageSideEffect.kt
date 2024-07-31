package com.bff.wespot.vote.state.storage

sealed class StorageSideEffect {
    data class NavigateToIndividualVote(
        val optionId: Int,
        val date: String,
        val isReceived: Boolean,
    ) :
        StorageSideEffect()
}
