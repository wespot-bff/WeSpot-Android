package com.bff.wespot.data.remote.source.firebase.messaging

interface MessagingDataSource {
    suspend fun getFcmToken(): String
}
