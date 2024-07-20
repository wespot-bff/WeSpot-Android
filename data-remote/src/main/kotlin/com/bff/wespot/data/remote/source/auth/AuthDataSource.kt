package com.bff.wespot.data.remote.source.auth

import com.bff.wespot.data.remote.model.auth.request.SignUpDto
import com.bff.wespot.data.remote.model.auth.response.AuthTokenDto
import com.bff.wespot.data.remote.model.auth.response.SchoolListDto

interface AuthDataSource {
    suspend fun getSchoolList(search: String): Result<SchoolListDto>
    suspend fun sendKakaoToken(token: com.bff.wespot.data.remote.model.auth.request.KakaoAuthTokenDto): Result<Any>
    suspend fun signUp(signUp: SignUpDto): Result<AuthTokenDto>
}