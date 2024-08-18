package com.bff.wespot.data.repository.auth

import com.bff.wespot.data.paging.auth.SchoolPagingSource
import com.bff.wespot.data.remote.source.auth.AuthDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.auth.response.School
import com.bff.wespot.model.common.Paging
import javax.inject.Inject

class SchoolPagingRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
) : BasePagingRepository<School, Paging<School>>() {
    override fun pagingSource(parameter: Map<String, String>?):
            BasePagingSource<School, Paging<School>> =
        SchoolPagingSource(authDataSource, parameter?.get("search"))
}