package com.bff.wespot.data.remote.source.user

import com.bff.wespot.network.extensions.safeRequest
import com.bff.wespot.data.remote.model.user.response.UserListDto
import com.bff.wespot.data.remote.model.user.response.ProfileDto
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : UserDataSource {
    override suspend fun getUserListByName(name: String): Result<UserListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("users/search")
                parameter("name", name)
            }
        }

    override suspend fun getProfile(): Result<ProfileDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("users/me")
            }
        }
}
