package com.bff.wespot.domain.repository.usecase

import com.bff.wespot.domain.repository.auth.KakaoLoginManager
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    val kakaoLoginManager: KakaoLoginManager,
) {
    // 나중에 authrepository랑 연결될 예정
    suspend inline fun invoke() {
        val result = kakaoLoginManager.loginWithKakao()
        println("token $result")
    }
}
