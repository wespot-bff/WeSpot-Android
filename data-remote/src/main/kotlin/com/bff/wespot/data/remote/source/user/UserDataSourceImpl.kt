package com.bff.wespot.data.remote.source.user

import com.bff.wespot.data.remote.model.user.response.NotificationSettingDto
import com.bff.wespot.network.extensions.safeRequest
import com.bff.wespot.data.remote.model.user.response.UserListDto
import com.bff.wespot.data.remote.model.user.response.ProfileDto
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : UserDataSource {
    override suspend fun getUserListByName(name: String, cursorId: Int): Result<UserListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("users/search")
                parameter("name", name)
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun getProfile(): Result<ProfileDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("users/me")
            }
        }

    override suspend fun getNotificationSetting(): Result<NotificationSettingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("users/settings")
            }
        }

    override suspend fun updateNotificationSetting(
        notificationSetting: NotificationSettingDto,
    ): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("users/settings")
            }
            setBody(NotificationSettingDto)
        }
}
