package com.bff.wespot.data.repository.community

import com.bff.wespot.data.paging.community.CommunitySearchPagingSource
import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CommunityContentPaging
import javax.inject.Inject

class CommunitySearchPagingRepository @Inject constructor(
    private val communityDataSource: CommunityDataSource
) : BasePagingRepository<BaseCommunityContent, CommunityContentPaging>() {
    override fun pagingSource(
        parameter: Map<String, String>?
    ): BasePagingSource<BaseCommunityContent, CommunityContentPaging> {
        val keyword = parameter?.get("keyword") ?: ""
        return CommunitySearchPagingSource(communityDataSource, keyword)
    }
}