package com.bff.wespot.domain.repository.community

import com.bff.wespot.model.community.PostComment
import com.bff.wespot.model.community.PostDetail

interface PostDetailRepository {
    suspend fun getPostDetail(postId: String): Result<PostDetail>
    suspend fun getPostComments(postId: String): Result<List<PostComment>>
    suspend fun registerNotification(postId: String): Boolean
    suspend fun sendComment(postId: Int, content: String): Boolean

    suspend fun reportComment(commentId: String): Boolean

    suspend fun likeComment(commentId: String): Boolean

    suspend fun deleteComment(commentId: String): Result<Unit>
    suspend fun deletePost(postId: String): Result<Unit>
    suspend fun blockPost(postId: String): Result<Unit>
}
