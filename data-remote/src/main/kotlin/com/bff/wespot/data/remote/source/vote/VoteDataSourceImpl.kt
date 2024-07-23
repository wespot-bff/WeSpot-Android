package com.bff.wespot.data.remote.source.vote

import io.ktor.client.HttpClient
import javax.inject.Inject

class VoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
): VoteDataSource {
}