package com.bff.wespot.vote.state.storage

sealed class StorageAction {
    data object GetReceivedVotes : StorageAction()
    data object GetSentVotes : StorageAction()
    data class ToIndividualVote(
        val optionId: Int, val date: String, val isReceived: Boolean
    ) :
        StorageAction()
}
