package com.bff.wespot.data.remote.config

interface RemoteConfigDataSource {
    suspend fun startRemoteConfig(): Boolean
    fun fetchFromRemoteConfig(key: String): String
}