package com.bff.wespot.data.remote.source.auth

import com.bff.wespot.data.remote.extensions.invalidateBearerTokens
import com.bff.wespot.data.remote.model.auth.request.KakaoAuthTokenDto
import com.bff.wespot.data.remote.model.auth.request.SignUpDto
import com.bff.wespot.data.remote.model.auth.response.AuthTokenDto
import com.bff.wespot.data.remote.model.auth.response.SchoolListDto
import com.bff.wespot.model.auth.request.RevokeReasonListDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : AuthDataSource {
    override suspend fun getSchoolList(search: String): Result<SchoolListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/schools/search")
                parameter("name", search)
            }
        }

    override suspend fun sendKakaoToken(token: KakaoAuthTokenDto): Result<Any> {
        val client = httpClient.safeRequest<Any> {
            url {
                method = HttpMethod.Post
                path("api/v1/auth/login")
            }
            setBody(token)
        }
        httpClient.invalidateBearerTokens()
        return client
    }

    override suspend fun signUp(signUp: SignUpDto): Result<AuthTokenDto> {
        val client = httpClient.safeRequest<AuthTokenDto> {
            url {
                method = HttpMethod.Post
                path("api/v1/auth/signup")
            }
            setBody(signUp)
        }

        httpClient.invalidateBearerTokens()
        return client
    }

    override suspend fun revoke(revokeReasonList: RevokeReasonListDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/auth/revoke")
            }
            setBody(revokeReasonList)
        }
}