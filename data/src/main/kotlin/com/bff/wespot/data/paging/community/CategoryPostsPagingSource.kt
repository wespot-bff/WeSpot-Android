package com.bff.wespot.data.paging.community

import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CategoryDetailsPaging
import com.bff.wespot.model.community.CommunityContentPaging
import com.bff.wespot.model.serverDriven.type.ImageType

class CategoryPostsPagingSource(
    private val communityDataSource: CommunityDataSource,
    private val categoryId: String,
    private val onImagesLoaded: (background: ImageType, thumbnail: ImageType) -> Unit = { _, _ -> }
) : BasePagingSource<BaseCommunityContent, CategoryDetailsPaging>() {
    override suspend fun fetchItems(cursorId: Int?): CategoryDetailsPaging {
        val response = communityDataSource.getCategoryPosts(categoryId, cursorId)
        val data = response.getOrThrow()
        val categoryDetailsPaging = data.toCategoryDetailsPaging()
        
        if (cursorId == null) {
            onImagesLoaded(categoryDetailsPaging.background, categoryDetailsPaging.thumbnail)
        }
        
        return categoryDetailsPaging
    }
}