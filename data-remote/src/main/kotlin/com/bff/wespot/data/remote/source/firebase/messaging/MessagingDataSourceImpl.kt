package com.bff.wespot.data.remote.source.firebase.messaging

import com.bff.wespot.data.remote.extensions.await
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject

class MessagingDataSourceImpl @Inject constructor(
    private val messaging: FirebaseMessaging,
) : MessagingDataSource {
    override suspend fun getFcmToken(): String {
        return suspendCancellableCoroutine { continuation ->
            messaging.token.addOnCompleteListener {
                continuation.resumeWith(Result.success(it.result))
            }.addOnFailureListener {
                Timber.e("Update FcmToken Failed Exception : ", it)
            }
        }
    }

    override suspend fun removeFcmToken() {
        messaging.isAutoInitEnabled = false
        messaging.deleteToken().await()
    }

    override suspend fun initMessaging() {
        messaging.isAutoInitEnabled = true
    }
}
