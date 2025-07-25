package com.bff.wespot.domain.repository.community

import com.bff.wespot.model.community.PostComment
import com.bff.wespot.model.community.PostDetail

interface PostDetailRepository {
    suspend fun getPostDetail(postId: String): Result<PostDetail>
    suspend fun getPostComments(postId: String): Result<List<PostComment>>
}
