package com.bff.wespot.domain.repository.firebase.config

interface RemoteConfigRepository {
    suspend fun startRemoteConfig(): Boolean
    fun fetchFromRemoteConfig(key: String): String
}
