package com.bff.wespot.data.repository

import com.bff.wespot.data.remote.model.common.EditProfileDto
import com.bff.wespot.data.remote.model.common.ProfanityDto
import com.bff.wespot.data.remote.model.common.ReportDto
import com.bff.wespot.data.remote.model.common.UpdateProfileDto
import com.bff.wespot.data.remote.source.CommonDataSource
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.Character
import com.bff.wespot.model.common.ReportType
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonDataSource: CommonDataSource
) : CommonRepository {
    override suspend fun checkProfanity(content: String): Result<Unit> =
        commonDataSource.checkProfanity(ProfanityDto(content))

    override suspend fun sendReport(report: ReportType, targetId: Int): Result<Unit> =
        commonDataSource.sendReport(ReportDto(targetId, report))

    override suspend fun getCharacters(): Result<List<Character>> =
        commonDataSource.getCharacters()
            .mapCatching { it.toCharacterList().characters }

    override suspend fun getBackgroundColors(): Result<List<BackgroundColor>> =
        commonDataSource.getBackgroundColors()
            .mapCatching { it.toBackgroundColorList().backgrounds }

    override suspend fun EditProfile(
        introduction: String,
        backgroundColor: String,
        iconUrl: String
    ): Result<Unit> =
        commonDataSource.EditProfile(
            EditProfileDto(
                introduction,
                UpdateProfileDto(
                    backgroundColor,
                    iconUrl
                )
            )
        )
}