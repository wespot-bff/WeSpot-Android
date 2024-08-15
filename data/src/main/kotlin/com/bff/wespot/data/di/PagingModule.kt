package com.bff.wespot.data.di

import com.bff.wespot.data.repository.vote.VoteReceivedPagingRepository
import com.bff.wespot.data.repository.vote.VoteSentPagingRepository
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.vote.response.ReceivedVoteData
import com.bff.wespot.model.vote.response.SentVoteData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PagingModule {
    @Binds
    @Singleton
    fun bindsVoteReceivedRepository(
        voteReceivedRepositoryImpl: VoteReceivedPagingRepository
    ): BasePagingRepository<ReceivedVoteData, Paging<ReceivedVoteData>>

    @Binds
    @Singleton
    fun bindsVoteSentRepository(
        voteSentRepositoryImpl: VoteSentPagingRepository
    ): BasePagingRepository<SentVoteData, Paging<SentVoteData>>
}