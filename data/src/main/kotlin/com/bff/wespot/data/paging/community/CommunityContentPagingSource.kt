package com.bff.wespot.data.paging.community

import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CommunityContentPaging

class CommunityContentPagingSource(
    private val communityDataSource: CommunityDataSource,
    private val inquirySize: Int,
    private val target: String,
) : BasePagingSource<BaseCommunityContent, CommunityContentPaging>() {
    
    companion object {
        private var globalItemsViewed: Int = 0
    }
    
    override suspend fun fetchItems(cursorId: Int?): CommunityContentPaging {

        val response = communityDataSource.getCommunityContent(
            target = target,
            inquirySize = inquirySize,
            cursorId = cursorId,
            countOfPostsViewed = globalItemsViewed
        )
        val data = response.getOrThrow()
        if (cursorId == null) {
            globalItemsViewed = 0
        }
        globalItemsViewed += data.content.size
        return data.toCommunityContentPaging()
    }
}