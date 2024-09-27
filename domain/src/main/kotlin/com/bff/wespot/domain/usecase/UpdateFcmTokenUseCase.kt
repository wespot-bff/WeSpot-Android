package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.firebase.messaging.MessagingRepository
import com.bff.wespot.domain.util.DataStoreKey
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val messagingRepository: MessagingRepository,
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            val token = messagingRepository.getFcmToken()
            dataStoreRepository.saveString(DataStoreKey.PUSH_TOKEN, token)
        }
    }
}
