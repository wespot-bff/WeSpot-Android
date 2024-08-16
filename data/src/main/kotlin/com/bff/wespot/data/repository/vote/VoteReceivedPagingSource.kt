package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.vote.response.ReceivedVoteData

class VoteReceivedPagingSource(
    private val voteDataSource: VoteDataSource
) : BasePagingSource<ReceivedVoteData, Paging<ReceivedVoteData>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<ReceivedVoteData> {
        val response = voteDataSource.getVoteReceived(cursorId)
        val data = response.getOrThrow()
        return data.toVoteReceived()
    }
}