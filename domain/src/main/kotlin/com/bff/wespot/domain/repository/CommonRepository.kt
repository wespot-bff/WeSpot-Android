package com.bff.wespot.domain.repository

interface CommonRepository {
    suspend fun checkProfanity(content: String): Result<Unit>
}