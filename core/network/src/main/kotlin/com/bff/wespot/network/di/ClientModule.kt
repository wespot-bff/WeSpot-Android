package com.bff.wespot.network.di

import android.util.Log
import com.bff.wespot.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {
    private const val REQUEST_TIMEOUT_MILLIS = 50_000L
    private const val CLIENT_TAG = "HttpClient"

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url(BuildConfig.MOCK_BASE_URL)
        }

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
                        Log.i(CLIENT_TAG, message)
                    }
                }
            }
        }
    }
}
