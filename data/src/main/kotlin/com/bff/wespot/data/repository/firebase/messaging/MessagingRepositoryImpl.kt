package com.bff.wespot.data.repository.firebase.messaging

import com.bff.wespot.data.remote.source.firebase.messaging.MessagingDataSource
import com.bff.wespot.domain.repository.firebase.messaging.MessagingRepository
import javax.inject.Inject

class MessagingRepositoryImpl @Inject constructor(
    private val messagingDataSource: MessagingDataSource,
): MessagingRepository {
    override suspend fun getFcmToken(): String = messagingDataSource.getFcmToken()
}
