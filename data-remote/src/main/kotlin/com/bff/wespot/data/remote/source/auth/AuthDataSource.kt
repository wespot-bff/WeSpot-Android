package com.bff.wespot.data.remote.source.auth

import com.bff.wespot.data.remote.model.auth.request.SignInDto
import com.bff.wespot.data.remote.model.auth.request.SignUpDto
import com.bff.wespot.data.remote.model.auth.response.AuthTokenDto
import com.bff.wespot.data.remote.model.auth.response.SchoolListDto
import com.bff.wespot.model.auth.request.RevokeReasonListDto

interface AuthDataSource {
    suspend fun getSchoolList(search: String, cursorId: Int?): Result<SchoolListDto>
    suspend fun signIn(signIn: SignInDto): Result<Any>
    suspend fun signUp(signUp: SignUpDto): Result<AuthTokenDto>
    suspend fun revoke(revokeReasonList: RevokeReasonListDto): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun agreeToPolicy(): Result<Unit>
}
