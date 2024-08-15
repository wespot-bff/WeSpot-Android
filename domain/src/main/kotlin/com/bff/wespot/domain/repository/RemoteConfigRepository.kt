package com.bff.wespot.domain.repository

interface RemoteConfigRepository {
    suspend fun startRemoteConfig(): Boolean
    fun fetchFromRemoteConfig(key: String): String
}