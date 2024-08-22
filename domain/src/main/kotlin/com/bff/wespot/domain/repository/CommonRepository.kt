package com.bff.wespot.domain.repository

import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.Character
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.common.ReportType

interface CommonRepository {
    suspend fun checkProfanity(content: String): Result<Unit>
    suspend fun sendReport(report: ReportType, targetId: Int): Result<Unit>
    suspend fun getCharacters(): Result<List<Character>>
    suspend fun getBackgroundColors(): Result<List<BackgroundColor>>
    suspend fun EditProfile(
        introduction: String,
        backgroundColor: String,
        iconUrl: String,
    ): Result<Unit>
    suspend fun getKakaoContent(type: String): Result<KakaoContent>
}
