package com.bff.wespot.vote.state.storage

import com.bff.wespot.model.vote.response.ReceivedVoteData
import com.bff.wespot.model.vote.response.SentVoteData

data class StorageUiState(
    val receivedVotes: List<ReceivedVoteData> = emptyList(),
    val sentVotes: List<SentVoteData> = emptyList(),
    val isLoading: Boolean = false,
)
