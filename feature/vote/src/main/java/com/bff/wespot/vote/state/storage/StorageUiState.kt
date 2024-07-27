package com.bff.wespot.vote.state.storage

import com.bff.wespot.model.vote.response.VoteData

data class StorageUiState (
    val receivedVotes: List<VoteData> = emptyList(),
)