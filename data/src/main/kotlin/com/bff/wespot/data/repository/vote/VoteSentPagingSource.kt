package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.vote.response.SentVoteData

class VoteSentPagingSource (
    private val voteDataSource: VoteDataSource
) : BasePagingSource<SentVoteData, Paging<SentVoteData>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<SentVoteData> {
        val response = voteDataSource.getVoteSent(cursorId)
        val data = response.getOrThrow()
        return data.toVoteSent()
    }
}