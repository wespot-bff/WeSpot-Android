package com.bff.wespot.data.repository.firebase.config

import com.bff.wespot.data.remote.source.firebase.config.RemoteConfigDataSource
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource
) : RemoteConfigRepository {
    override suspend fun startRemoteConfig(): Boolean =
        remoteConfigDataSource.startRemoteConfig()

    override fun fetchFromRemoteConfig(key: String): String =
        remoteConfigDataSource.fetchFromRemoteConfig(key)
}