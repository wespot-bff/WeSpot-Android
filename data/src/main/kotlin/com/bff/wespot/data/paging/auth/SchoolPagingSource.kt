package com.bff.wespot.data.paging.auth

import com.bff.wespot.data.remote.source.auth.AuthDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.auth.response.School
import com.bff.wespot.model.auth.response.SchoolList
import com.bff.wespot.model.common.Paging

class SchoolPagingSource(
    private val authDataSource: AuthDataSource,
    private val search: String?,
) : BasePagingSource<School, Paging<School>>() {
    override suspend fun fetchItems(cursorId: Int?): Paging<School> {
        search ?: return SchoolList(emptyList(), -1, false)
        val response = authDataSource.getSchoolList(search, cursorId)
        val data = response.getOrThrow()
        return data.toSchoolList()
    }
}