package com.bff.wespot.data.repository.community

import com.bff.wespot.data.paging.community.CategoryPostsPagingSource
import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CommunityContentPaging
import javax.inject.Inject

class CategoryPostsPagingRepository @Inject constructor(
    private val communityDataSource: CommunityDataSource
) : BasePagingRepository<BaseCommunityContent, CommunityContentPaging>() {
    override fun pagingSource(
        parameter: Map<String, String>?
    ): BasePagingSource<BaseCommunityContent, CommunityContentPaging> {
        val categoryId = parameter?.get("categoryId") ?: ""
        return CategoryPostsPagingSource(communityDataSource, categoryId)
    }
}