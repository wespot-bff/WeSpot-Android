package com.bff.wespot.data.repository.community

import com.bff.wespot.data.paging.community.CategoryPostsPagingSource
import com.bff.wespot.data.remote.source.community.CommunityDataSource
import com.bff.wespot.domain.paging.BasePagingSource
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.CategoryDetailsPaging
import com.bff.wespot.model.community.CommunityContentPaging
import com.bff.wespot.model.serverDriven.type.ImageType
import javax.inject.Inject

class CategoryPostsPagingRepository @Inject constructor(
    private val communityDataSource: CommunityDataSource
) : BasePagingRepository<BaseCommunityContent, CategoryDetailsPaging>() {
    
    private var onImagesLoaded: ((ImageType, ImageType) -> Unit)? = null
    
    fun setOnImagesLoadedCallback(callback: (ImageType, ImageType) -> Unit) {
        onImagesLoaded = callback
    }
    
    override fun pagingSource(
        parameter: Map<String, String>?
    ): BasePagingSource<BaseCommunityContent, CategoryDetailsPaging> {
        val categoryId = parameter?.get("categoryId") ?: ""
        return CategoryPostsPagingSource(
            communityDataSource, 
            categoryId,
            onImagesLoaded ?: { _, _ -> }
        )
    }
}