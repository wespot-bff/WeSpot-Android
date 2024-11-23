package com.bff.wespot.domain.repository.auth

import com.bff.wespot.model.auth.request.SignIn
import com.bff.wespot.model.auth.request.SignUp

interface AuthRepository {
    suspend fun signIn(signIn: SignIn): Result<Any>
    suspend fun signUp(signUp: SignUp): Boolean
    suspend fun revoke(revokeReasonList: List<String>): Result<Unit>
}
