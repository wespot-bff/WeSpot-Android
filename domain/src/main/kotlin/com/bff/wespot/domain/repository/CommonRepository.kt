package com.bff.wespot.domain.repository

import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.common.ReportType
import com.bff.wespot.model.common.Restriction

interface CommonRepository {
    suspend fun checkProfanity(content: String): Result<Unit>
    suspend fun sendReport(report: ReportType, targetId: Int, content: String? = null): Result<Unit>
    suspend fun getKakaoContent(type: String): Result<KakaoContent>
    suspend fun getRestriction(): Result<Restriction>
    suspend fun uploadImage(imagePath: String): Result<String>
}
