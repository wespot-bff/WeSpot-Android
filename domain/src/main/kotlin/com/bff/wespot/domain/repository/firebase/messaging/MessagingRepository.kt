package com.bff.wespot.domain.repository.firebase.messaging

interface MessagingRepository {
    suspend fun getFcmToken(): String
}
