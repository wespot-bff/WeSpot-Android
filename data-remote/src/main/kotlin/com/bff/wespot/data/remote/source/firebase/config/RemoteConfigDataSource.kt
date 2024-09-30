package com.bff.wespot.data.remote.source.firebase.config

interface RemoteConfigDataSource {
    suspend fun startRemoteConfig(): Boolean
    fun fetchFromRemoteConfig(key: String): String
}