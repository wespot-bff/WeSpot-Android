package com.bff.wespot.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bff.wespot.data.local.database.dao.MessageDao
import com.bff.wespot.data.local.model.message.MessageEntity
import com.bff.wespot.data.paging.message.MessageRemoteMediator
import com.bff.wespot.data.remote.source.message.MessageStorageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MessagesPager

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookmarkedMessagesPager

@Module
@InstallIn(SingletonComponent::class)
object PagerModule {

    @OptIn(ExperimentalPagingApi::class)
    @MessagesPager
    @Provides
    @Singleton
    fun providesMessagesPager(
        source: MessageStorageDataSource,
        dao: MessageDao,
    ): Pager<Int, MessageEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MessageRemoteMediator(
                source = source,
                dao = dao,
            ),
            pagingSourceFactory = {
                dao.getPagingSource()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @BookmarkedMessagesPager
    @Provides
    @Singleton
    fun providesBookmarkedMessagesPager(
        source: MessageStorageDataSource,
        dao: MessageDao,
    ): Pager<Int, MessageEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MessageRemoteMediator(
                source = source,
                dao = dao,
            ),
            pagingSourceFactory = {
                dao.getBookmarkedMessagePagingSource()
            }
        )
    }
}