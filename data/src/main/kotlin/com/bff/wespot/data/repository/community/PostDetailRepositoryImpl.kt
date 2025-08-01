package com.bff.wespot.data.repository.community

import com.bff.wespot.data.remote.source.community.PostDetailDataSource
import com.bff.wespot.domain.repository.community.PostDetailRepository
import com.bff.wespot.model.community.PostComment
import com.bff.wespot.model.community.PostDetail
import javax.inject.Inject

class PostDetailRepositoryImpl @Inject constructor(
    private val postDetailDataSource: PostDetailDataSource
) : PostDetailRepository {
    override suspend fun getPostDetail(postId: String): Result<PostDetail> =
        postDetailDataSource.getPostDetail(postId)
            .mapCatching {
                it.toDomain()
            }

    override suspend fun getPostComments(postId: String): Result<List<PostComment>> =
        postDetailDataSource.getPostComments(postId)
            .mapCatching {
                it.map { it.toDomain() }
            }

    override suspend fun registerNotification(postId: String): Boolean {
        return postDetailDataSource.registerNotification(postId).isSuccess
    }

    override suspend fun sendComment(postId: Int, content: String): Boolean {
        return postDetailDataSource.sendComment(postId, content).isSuccess
    }

    override suspend fun reportComment(commentId: String): Boolean {
        return postDetailDataSource.reportComment(commentId).isSuccess
    }

    override suspend fun likeComment(commentId: String): Boolean {
        return postDetailDataSource.likeComment(commentId).isSuccess
    }
}