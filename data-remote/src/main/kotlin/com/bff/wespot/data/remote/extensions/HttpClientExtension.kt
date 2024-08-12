package com.bff.wespot.data.remote.extensions

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import timber.log.Timber

fun HttpClient.invalidateBearerTokens() {
    try {
        plugin(Auth).providers
            .filterIsInstance<BearerAuthProvider>()
            .first().clearToken()
    } catch (e: IllegalStateException) {
        Timber.e(e)
    }
}