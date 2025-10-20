package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.PostInfoDto
import com.bff.wespot.data.remote.model.community.chip.CategoryChipsDto

interface WritePostDataSource {
    suspend fun getCategories(): Result<List<CategoryChipsDto>>
    suspend fun createPost(info: PostInfoDto): Result<Unit>
    suspend fun editPost(postId: String, info: PostInfoDto): Result<Unit>
}