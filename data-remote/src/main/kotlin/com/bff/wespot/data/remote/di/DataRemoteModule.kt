package com.bff.wespot.data.remote.di

import com.bff.wespot.data.remote.source.firebase.config.RemoteConfigDataSource
import com.bff.wespot.data.remote.source.firebase.config.RemoteConfigDataSourceImpl
import com.bff.wespot.data.remote.source.CommonDataSource
import com.bff.wespot.data.remote.source.CommonDataSourceImpl
import com.bff.wespot.data.remote.source.auth.AuthDataSource
import com.bff.wespot.data.remote.source.auth.AuthDataSourceImpl
import com.bff.wespot.data.remote.source.firebase.messaging.MessagingDataSource
import com.bff.wespot.data.remote.source.firebase.messaging.MessagingDataSourceImpl
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.data.remote.source.message.MessageDataSourceImpl
import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import com.bff.wespot.data.remote.source.message.MessageStorageDataSourceImpl
import com.bff.wespot.data.remote.source.notification.NotificationDataSource
import com.bff.wespot.data.remote.source.notification.NotificationDataSourceImpl
import com.bff.wespot.data.remote.source.user.UserDataSource
import com.bff.wespot.data.remote.source.user.UserDataSourceImpl
import com.bff.wespot.data.remote.source.vote.VoteDataSource
import com.bff.wespot.data.remote.source.vote.VoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRemoteModule {
    @Binds
    @Singleton
    abstract fun bindsMessageDataSource(
        messageDataSourceImpl: MessageDataSourceImpl
    ): MessageDataSource

    @Binds
    @Singleton
    abstract fun bindsAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindsVoteDataSource(
        voteDataSourceImpl: VoteDataSourceImpl
    ): VoteDataSource

    @Binds
    @Singleton
    abstract fun bindsUserDataSource(
        userDataSourceImpl: UserDataSourceImpl
    ): UserDataSource

    @Binds
    @Singleton
    abstract fun bindsMessageStorageDataSource(
        messageStorageDataSourceImpl: MessageStorageDataSourceImpl
    ): MessageStorageDataSource

    @Binds
    @Singleton
    abstract fun bindsNotificationDataSource(
        notificationDataSourceImpl: NotificationDataSourceImpl
    ): NotificationDataSource

    @Binds
    @Singleton
    abstract fun bindsCommonDataSource(
        commonDataSourceImpl: CommonDataSourceImpl
    ): CommonDataSource

    @Binds
    @Singleton
    abstract fun bindsRemoteConfigDataSource(
        remoteConfigDataSourceImpl: RemoteConfigDataSourceImpl
    ): RemoteConfigDataSource

    @Binds
    @Singleton
    abstract fun bindsMessagingDataSource(
        messagingDataSourceImpl: MessagingDataSourceImpl
    ): MessagingDataSource
}
