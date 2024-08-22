package com.bff.wespot.domain.repository.auth

import com.bff.wespot.model.auth.request.KakaoAuthToken

interface KakaoLoginManager {
    suspend fun loginWithKakao(): KakaoAuthToken
}
