package com.bff.wespot.domain.repository.auth

import com.bff.wespot.model.auth.KakaoAuthToken

interface KakaoLoginManager {
    suspend fun loginWithKakao(): KakaoAuthToken
}
