package com.bff.wespot.data.repository.auth

import com.bff.wespot.data.mapper.auth.toDto
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.response.AuthToken
import com.bff.wespot.model.auth.response.School
import com.bff.wespot.network.model.auth.response.SchoolDto
import com.bff.wespot.network.source.auth.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun getSchoolList(search: String): Result<List<School>> =
        authDataSource
            .getSchoolList(search)
            .map { it.schools.map(SchoolDto::toSchool) }

    override suspend fun sendKakaoToken(token: KakaoAuthToken): Result<AuthToken> =
        authDataSource
            .sendKakaoToken(token.toDto())
            .map { it.toAuthToken() }
}