package com.bff.wespot.domain.repository

import com.bff.wespot.model.ReportType

interface CommonRepository {
    suspend fun checkProfanity(content: String): Result<Unit>
    suspend fun sendReport(report: ReportType, targetId: Int): Result<Unit>
}
