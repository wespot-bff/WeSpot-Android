package com.bff.wespot.data.remote.source

import com.bff.wespot.data.remote.model.common.BackgroundColorListDto
import com.bff.wespot.data.remote.model.common.CharacterListDto
import com.bff.wespot.data.remote.model.common.EditProfileDto
import com.bff.wespot.data.remote.model.common.KakaoContentDto
import com.bff.wespot.data.remote.model.common.ProfanityDto
import com.bff.wespot.data.remote.model.common.ReportDto
import com.bff.wespot.data.remote.model.common.RestrictionDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class CommonDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : CommonDataSource {
    override suspend fun checkProfanity(content: ProfanityDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/check-profanity")
                setBody(content)
            }
        }

    override suspend fun sendReport(report: ReportDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/reports")
                setBody(report)
            }
        }

    override suspend fun getCharacters(): Result<CharacterListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/users/characters")
            }
        }

    override suspend fun getBackgroundColors(): Result<BackgroundColorListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/users/backgrounds")
            }
        }

    override suspend fun EditProfile(profile: EditProfileDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Put
                path("api/v1/users/me")
                setBody(profile)
            }
        }

    override suspend fun getKakaoContent(type: String): Result<KakaoContentDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/auth/kakao/template")
                parameter("type", type.lowercase())
            }
        }

    override suspend fun checkRestriction(): Result<RestrictionDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/users/restrictions/me")
            }
        }
}
