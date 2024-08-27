package com.bff.wespot.data.remote.source.user

import com.bff.wespot.data.remote.model.user.request.FeatureNotificationSettingDto
import com.bff.wespot.data.remote.model.user.request.IntroductionDto
import com.bff.wespot.data.remote.model.user.response.NotificationSettingDto
import com.bff.wespot.data.remote.model.user.response.ProfileCharacterDto
import com.bff.wespot.data.remote.model.user.response.ProfileDto
import com.bff.wespot.data.remote.model.user.response.UserListDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : UserDataSource {
    override suspend fun getUserListByName(name: String, cursorId: Int?): Result<UserListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/users/search")
                parameter("name", name)
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun getProfile(): Result<ProfileDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/users/me")
            }
        }

    override suspend fun getNotificationSetting(): Result<NotificationSettingDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/users/settings")
            }
        }

    override suspend fun setFeatureNotificationSetting(
        featureNotificationSettingDto: FeatureNotificationSettingDto
    ): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/users/settings")
            }
            setBody(featureNotificationSettingDto)
        }

    override suspend fun updateNotificationSetting(
        notificationSetting: NotificationSettingDto,
    ): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/users/settings")
            }
            setBody(notificationSetting)
        }

    override suspend fun updateIntroduction(introduction: IntroductionDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/users/me")
            }
            setBody(introduction)
        }

    override suspend fun updateCharacter(character: ProfileCharacterDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/users/me")
            }
            setBody(character)
        }
}
