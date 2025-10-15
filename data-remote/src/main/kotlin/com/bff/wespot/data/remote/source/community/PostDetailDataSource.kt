package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.PostCommentDto
import com.bff.wespot.data.remote.model.community.PostDetailDto

interface PostDetailDataSource {
    suspend fun getPostDetail(postId: String): Result<PostDetailDto>
    suspend fun getPostComments(postId: String): Result<List<PostCommentDto>>
}