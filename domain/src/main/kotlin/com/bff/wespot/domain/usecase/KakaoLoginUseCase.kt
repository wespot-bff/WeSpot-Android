package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.repository.auth.KakaoLoginManager
import com.bff.wespot.domain.util.DataStoreKey
import javax.inject.Inject
import javax.xml.crypto.Data

class KakaoLoginUseCase @Inject constructor(
    val kakaoLoginManager: KakaoLoginManager,
    val authRepository: AuthRepository,
    val dataStoreRepository: DataStoreRepository
) {
    suspend inline fun invoke() {
        val result = kakaoLoginManager.loginWithKakao()
        authRepository.sendKakaoToken(result)
            .onSuccess {
                dataStoreRepository.saveString(DataStoreKey.ACCESS_TOKEN, it.accessToken)
                dataStoreRepository.saveString(DataStoreKey.REFRESH_TOKEN, it.refreshToken)
            }
            .onFailure {
                throw(it)
            }
    }
}
