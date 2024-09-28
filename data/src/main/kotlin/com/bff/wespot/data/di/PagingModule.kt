package com.bff.wespot.data.di

import com.bff.wespot.data.repository.auth.SchoolPagingRepository
import com.bff.wespot.data.repository.message.MessageBlockedPagingRepository
import com.bff.wespot.data.repository.message.MessageReceivedPagingRepository
import com.bff.wespot.data.repository.message.MessageSentPagingRepository
import com.bff.wespot.data.repository.notification.NotificationPagingRepository
import com.bff.wespot.data.repository.user.UserPagingRepository
import com.bff.wespot.data.repository.vote.VoteReceivedPagingRepository
import com.bff.wespot.data.repository.vote.VoteSentPagingRepository
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.auth.response.School
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.response.BlockedMessage
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.notification.Notification
import com.bff.wespot.model.user.response.User
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

    @Binds
    @Singleton
    fun bindsSchoolRepository(
        schoolRepositoryImpl: SchoolPagingRepository
    ): BasePagingRepository<School, Paging<School>>

    @Binds
    @Singleton
    fun bindsMessageSentRepository(
        messageSentRepositoryImpl: MessageSentPagingRepository
    ): BasePagingRepository<Message, Paging<Message>>

    @Binds
    @Singleton
    fun bindsMessageReceivedRepository(
        messageReceivedRepositoryImpl: MessageReceivedPagingRepository
    ): BasePagingRepository<ReceivedMessage, Paging<ReceivedMessage>>

    @Binds
    @Singleton
    fun bindsUserRepository(
        userRepositoryImpl: UserPagingRepository
    ): BasePagingRepository<User, Paging<User>>

    @Binds
    @Singleton
    fun bindsNotificationRepository(
        notificationRepositoryImpl: NotificationPagingRepository
    ): BasePagingRepository<Notification, Paging<Notification>>

    @Binds
    @Singleton
    fun bindsMessageBlockedRepository(
        blockedMessageRepositoryImpl: MessageBlockedPagingRepository,
    ): BasePagingRepository<BlockedMessage, Paging<BlockedMessage>>
}
