package com.bff.wespot.data.remote.source

import com.bff.wespot.data.remote.model.ProfanityDto
import com.bff.wespot.data.remote.model.ReportDto

interface CommonDataSource {
    suspend fun checkProfanity(content: ProfanityDto): Result<Unit>
    suspend fun sendReport(report: ReportDto): Result<Unit>
}