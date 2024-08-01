package com.bff.wespot.data.repository

import com.bff.wespot.data.remote.model.ProfanityDto
import com.bff.wespot.data.remote.source.CommonDataSource
import com.bff.wespot.domain.repository.CommonRepository
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonDataSource: CommonDataSource
): CommonRepository {
    override suspend fun checkProfanity(content: String): Result<Unit> =
        commonDataSource.checkProfanity(ProfanityDto(content))
}