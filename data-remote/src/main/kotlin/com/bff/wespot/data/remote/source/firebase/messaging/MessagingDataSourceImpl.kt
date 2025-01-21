package com.bff.wespot.data.remote.source.firebase.messaging

import com.bff.wespot.data.remote.extensions.await
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class MessagingDataSourceImpl @Inject constructor(
    private val messaging: FirebaseMessaging,
) : MessagingDataSource {
    override suspend fun getFcmToken(): String {
        messaging.isAutoInitEnabled = true
        return messaging.token.await()
    }

    override suspend fun removeFcmToken() {
        messaging.isAutoInitEnabled = false
        messaging.deleteToken().await()
    }
}
