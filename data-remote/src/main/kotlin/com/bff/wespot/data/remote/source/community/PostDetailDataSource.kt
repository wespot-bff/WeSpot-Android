package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.PostCommentDto
import com.bff.wespot.data.remote.model.community.PostDetailDto

interface PostDetailDataSource {
    suspend fun getPostDetail(postId: String): Result<PostDetailDto>
    suspend fun getPostComments(postId: String): Result<List<PostCommentDto>>
    suspend fun registerNotification(postId: String): Result<Unit>

    suspend fun sendComment(postId: Int, content: String): Result<Unit>

    suspend fun reportComment(commentId: String): Result<Unit>

    suspend fun likeComment(commentId: String): Result<Unit>
    
    suspend fun deleteComment(commentId: String): Result<Unit>
    suspend fun deletePost(postId: String): Result<Unit>
    suspend fun blockPost(postId: String): Result<Unit>
}