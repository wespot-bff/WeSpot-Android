package com.bff.wespot.data.remote.source

import com.bff.wespot.data.remote.model.common.EditProfileDto
import com.bff.wespot.data.remote.model.common.ImageUrlDto
import com.bff.wespot.data.remote.model.common.KakaoContentDto
import com.bff.wespot.data.remote.model.common.ProfanityDto
import com.bff.wespot.data.remote.model.common.ReportDto
import com.bff.wespot.data.remote.model.common.RestrictionDto

interface CommonDataSource {
    suspend fun checkProfanity(content: ProfanityDto): Result<Unit>
    suspend fun sendReport(report: ReportDto): Result<Unit>
    suspend fun editProfile(
        profile: EditProfileDto
    ): Result<Unit>

    suspend fun getKakaoContent(type: String): Result<KakaoContentDto>

    suspend fun checkRestriction(): Result<RestrictionDto>

    suspend fun getPresignedUrl(mimeType: String): Result<ImageUrlDto>

    suspend fun uploadImage(url: String, imagePath: String): Boolean
}