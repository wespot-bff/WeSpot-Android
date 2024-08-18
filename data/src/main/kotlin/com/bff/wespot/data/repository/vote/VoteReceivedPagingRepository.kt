package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.paging.vote.VoteReceivedPagingSource
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.vote.response.ReceivedVoteData
import javax.inject.Inject

class VoteReceivedPagingRepository @Inject constructor(
    private val voteDataSource: VoteDataSource
) : BasePagingRepository<ReceivedVoteData, Paging<ReceivedVoteData>>() {
    override fun pagingSource(parameter: Map<String, String>?): BasePagingSource<ReceivedVoteData, Paging<ReceivedVoteData>> =
        VoteReceivedPagingSource(voteDataSource)
}