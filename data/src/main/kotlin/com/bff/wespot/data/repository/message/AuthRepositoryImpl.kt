package com.bff.wespot.data.repository.message

import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.model.auth.School
import com.bff.wespot.network.model.auth.SchoolDto
import com.bff.wespot.network.source.auth.AuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun getSchoolList(search: String): Result<List<School>> =
        authDataSource
            .getSchoolList(search)
            .map { it.schools.map(SchoolDto::toSchool) }

}