package com.bff.wespot.data.remote.di

import com.bff.wespot.data.remote.BuildConfig
import com.bff.wespot.data.remote.model.auth.response.AuthTokenDto
import com.bff.wespot.data.remote.model.auth.response.SignUpTokenDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {
    private const val REQUEST_TIMEOUT_MILLIS = 50_000L

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url("https://2121b79d-93e5-4932-9e8b-45230a5f2647.mock.pstmn.io/")
        }

        // TODO Token 연동
        /*install(Auth) {
            bearer {
                refreshTokens {
                    BearerTokens(
                        accessToken = token.bearerToken,
                        refreshToken = token.refreshToken
                    )
                }
            }
        }*/

        HttpResponseValidator {
            this.
            validateResponse {
                if(it.request.url.toString().contains("api/v1/auth/login")) {
                    when(it.status.value) {
                        200 -> {
                            val parsedBody = it.body<AuthTokenDto>()
                            it.call.attributes.put(AttributeKey("parsedBody"), parsedBody)
                        }
                        202 -> {
                            val parsedBody = it.body<SignUpTokenDto>()
                            it.call.attributes.put(AttributeKey("parsedBody"), parsedBody)
                        }
                    }
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    encodeDefaults = true // Default 값 포함한 직렬화 수행
                    isLenient = true // 비표준 Json 형식 허용
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    if (BuildConfig.DEBUG) {
                        Timber.i(message)
                    }
                }
            }
        }
    }
}
