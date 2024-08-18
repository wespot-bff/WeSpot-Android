package com.bff.wespot.domain.repository.auth

import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.request.SignUp

interface AuthRepository {
    suspend fun sendKakaoToken(token: KakaoAuthToken): Result<Any>
    suspend fun signUp(signUp: SignUp): Boolean
    suspend fun revoke(revokeReasonList: List<String>): Result<Unit>
}
