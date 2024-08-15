package com.bff.wespot.vote.state.storage

import androidx.paging.PagingData
import com.bff.wespot.model.vote.response.ReceivedVoteData
import com.bff.wespot.model.vote.response.SentVoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class StorageUiState(
    val receivedVotes: Flow<PagingData<ReceivedVoteData>> = flow { },
    val sentVotes: Flow<PagingData<SentVoteData>> = flow { },
    val isLoading: Boolean = false,
)
