package com.bff.wespot.data.repository.vote

import com.bff.wespot.data.paging.vote.VoteSentPagingSource
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.vote.response.SentVoteData
import javax.inject.Inject

class VoteSentPagingRepository @Inject constructor(
    private val voteDataSource: VoteDataSource
) : BasePagingRepository<SentVoteData, Paging<SentVoteData>>() {
    override fun pagingSource(parameter: Map<String, String>?):
            BasePagingSource<SentVoteData, Paging<SentVoteData>> =
        VoteSentPagingSource(voteDataSource)
}