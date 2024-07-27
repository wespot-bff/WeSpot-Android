package com.bff.wespot.vote.state.storage

sealed class StorageAction {
    data object GetReceivedVotes : StorageAction()
    data object GetSentVotes : StorageAction()
}