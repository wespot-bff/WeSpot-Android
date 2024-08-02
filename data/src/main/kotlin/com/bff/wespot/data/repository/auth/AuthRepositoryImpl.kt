package com.bff.wespot.data.repository.auth

import com.bff.wespot.data.local.source.WeSpotDataStore
import com.bff.wespot.data.mapper.auth.toDto
import com.bff.wespot.data.remote.model.auth.response.AuthTokenDto
import com.bff.wespot.data.remote.model.auth.response.SchoolDto
import com.bff.wespot.data.remote.model.auth.response.SignUpTokenDto
import com.bff.wespot.data.remote.source.auth.AuthDataSource
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.request.RevokeReasonListDto
import com.bff.wespot.model.auth.request.SignUp
import com.bff.wespot.model.auth.response.School
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val dataStore: WeSpotDataStore
) : AuthRepository {
    override suspend fun getSchoolList(search: String): Result<List<School>> =
        authDataSource
            .getSchoolList(search)
            .map { it.schools.map(SchoolDto::toSchool) }

    override suspend fun sendKakaoToken(token: KakaoAuthToken): Result<Any> =
        authDataSource
            .sendKakaoToken(token.toDto())
            .map {
                when (it) {
                    is AuthTokenDto -> {
                        it.toAuthToken()
                    }

                    is SignUpTokenDto -> {
                        it.toSignUpToken()
                    }

                    else -> throw IllegalArgumentException("Unknown token type")
                }
            }

    override suspend fun signUp(signUp: SignUp): Boolean {
        return authDataSource
            .signUp(
                signUp.toDto(
                    dataStore.getString(DataStoreKey.SIGN_UP_TOKEN).first()
                )
            ).isSuccess
    }

    override suspend fun revoke(revokeReasonList: List<String>): Result<Unit> {
        return authDataSource.revoke(
            RevokeReasonListDto(revokeReasons = revokeReasonList)
        )
    }
}