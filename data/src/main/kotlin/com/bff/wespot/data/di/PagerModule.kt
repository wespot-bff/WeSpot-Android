package com.bff.wespot.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bff.wespot.data.local.database.dao.ReceivedMessageDao
import com.bff.wespot.data.local.model.message.ReceivedMessageEntity
import com.bff.wespot.data.paging.message.MessageReceivedRemoteMediator
import com.bff.wespot.data.remote.source.message.MessageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagerModule {

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providesReceivedMessagePager(
        source: MessageDataSource,
        dao: ReceivedMessageDao,
    ): Pager<Int, ReceivedMessageEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = MessageReceivedRemoteMediator(
                source = source,
                dao = dao,
            ),
            pagingSourceFactory = {
                dao.pagingSource()
            }
        )
    }
}
