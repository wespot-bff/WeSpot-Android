package com.bff.wespot.data.repository

import com.bff.wespot.data.local.database.dao.MessageDao
import com.bff.wespot.data.remote.model.ImageUploadFailedException
import com.bff.wespot.data.remote.model.common.ProfanityDto
import com.bff.wespot.data.remote.model.common.ReportDto
import com.bff.wespot.data.remote.source.CommonDataSource
import com.bff.wespot.data.remote.source.ImageDecoderDataSource
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.common.ReportType
import com.bff.wespot.model.common.Restriction
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonDataSource: CommonDataSource,
    private val decoder: ImageDecoderDataSource,
    private val messageDao: MessageDao,
) : CommonRepository {
    override suspend fun checkProfanity(content: String): Result<Unit> =
        commonDataSource.checkProfanity(ProfanityDto(content))

    override suspend fun sendReport(
        report: ReportType,
        targetId: Int,
        content: String?,
    ): Result<Unit> = commonDataSource.sendReport(ReportDto(targetId, report, content)).onSuccess {
        if (report == ReportType.MESSAGE) {
            messageDao.deleteMessage(targetId)
        }
    }

    override suspend fun getKakaoContent(type: String): Result<KakaoContent> =
        commonDataSource.getKakaoContent(type)
            .mapCatching { it.toKakaoContent() }

    override suspend fun getRestriction(): Result<Restriction> =
        commonDataSource.checkRestriction()
            .mapCatching { it.toRestriction() }

    override suspend fun uploadImage(imagePath: String): Result<String> {
        val decodeImage = decoder.decodeImage(imagePath)
        val url = commonDataSource.getPresignedUrl("webp").getOrNull()
            ?: throw ImageUploadFailedException.UploadFailedException("Failed to get presigned url")
        val result = commonDataSource.uploadImage(url.url, decodeImage)

        return if (result) {
            Result.success(url.imageName)
        } else {
            Result.failure(
                ImageUploadFailedException.UploadFailedException("Failed to upload image")
            )
        }
    }
}