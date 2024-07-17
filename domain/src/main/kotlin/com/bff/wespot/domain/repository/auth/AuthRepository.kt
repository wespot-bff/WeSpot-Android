package com.bff.wespot.domain.repository.auth

import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.response.AuthToken
import com.bff.wespot.model.auth.response.School

interface AuthRepository {
    suspend fun getSchoolList(search: String): Result<List<School>>
    suspend fun sendKakaoToken(token: KakaoAuthToken): Result<AuthToken>
}
