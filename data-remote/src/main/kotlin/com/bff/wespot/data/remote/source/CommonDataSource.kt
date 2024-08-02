package com.bff.wespot.data.remote.source

import com.bff.wespot.data.remote.model.common.BackgroundColorListDto
import com.bff.wespot.data.remote.model.common.CharacterListDto
import com.bff.wespot.data.remote.model.common.EditProfileDto
import com.bff.wespot.data.remote.model.common.ProfanityDto
import com.bff.wespot.data.remote.model.common.ReportDto

interface CommonDataSource {
    suspend fun checkProfanity(content: ProfanityDto): Result<Unit>
    suspend fun sendReport(report: ReportDto): Result<Unit>
    suspend fun getCharacters(): Result<CharacterListDto>
    suspend fun getBackgroundColors(): Result<BackgroundColorListDto>
    suspend fun EditProfile(
        profile: EditProfileDto
    ): Result<Unit>
}