package com.bff.wespot.network.source.auth

import com.bff.wespot.network.model.auth.request.KakaoAuthTokenDto
import com.bff.wespot.network.model.auth.response.AuthTokenDto
import com.bff.wespot.network.model.auth.response.SchoolListDto

interface AuthDataSource {
    suspend fun getSchoolList(search: String): Result<SchoolListDto>
    suspend fun sendKakaoToken(token: KakaoAuthTokenDto): Result<AuthTokenDto>
}