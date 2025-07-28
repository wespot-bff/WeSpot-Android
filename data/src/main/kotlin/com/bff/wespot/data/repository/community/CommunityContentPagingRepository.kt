package com.bff.wespot.data.repository.community

import com.bff.wespot.data.paging.community.CommunityContentPagingSource
import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CommunityContentPaging
import javax.inject.Inject

class CommunityContentPagingRepository @Inject constructor(
    private val communityDataSource: CommunityDataSource
) : BasePagingRepository<BaseCommunityContent, CommunityContentPaging>() {
    override fun pagingSource(
        parameter: Map<String, String>?
    ): BasePagingSource<BaseCommunityContent, CommunityContentPaging> {
        val inquirySize = parameter?.get("inquiry_size")?.toIntOrNull() ?: -1
        val target = parameter?.get("target") ?: ""
        return CommunityContentPagingSource(communityDataSource, inquirySize, target)
    }
}