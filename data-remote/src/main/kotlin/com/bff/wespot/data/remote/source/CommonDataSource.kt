package com.bff.wespot.data.remote.source

import com.bff.wespot.data.remote.model.ProfanityDto

interface CommonDataSource {
    suspend fun checkProfanity(content: ProfanityDto): Result<Unit>
}