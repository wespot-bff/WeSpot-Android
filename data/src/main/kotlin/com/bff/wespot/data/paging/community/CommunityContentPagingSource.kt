package com.bff.wespot.data.paging.community

import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CommunityContentPaging

class CommunityContentPagingSource(
    private val communityDataSource: CommunityDataSource
) : BasePagingSource<BaseCommunityContent, CommunityContentPaging>() {
    override suspend fun fetchItems(cursorId: Int?): CommunityContentPaging {
        val response = communityDataSource.getCommunityContent(cursorId)
        val data = response.getOrThrow()
        return data.toCommunityContentPaging()
    }
}