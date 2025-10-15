package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.community.CommentRequest
import com.bff.wespot.data.remote.model.community.PostCommentDto
import com.bff.wespot.data.remote.model.community.PostDetailDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.path
import javax.inject.Inject

class PostDetailDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : PostDetailDataSource {
    override suspend fun getPostDetail(postId: String): Result<PostDetailDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post/$postId")
            }
        }

    override suspend fun getPostComments(postId: String): Result<List<PostCommentDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/post/comment")
                parameter("postId", postId)
            }
        }

    override suspend fun registerNotification(postId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/${postId}/notification/comment")
            }
        }

    override suspend fun sendComment(postId: Int, content: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/comment")
                contentType(ContentType.Application.Json)
                setBody(CommentRequest(postId = postId, content = content))
            }
        }

    override suspend fun reportComment(commentId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/comment/${commentId}/report")
            }
        }

    override suspend fun likeComment(commentId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/comment/${commentId}/like")
            }
        }
    
    override suspend fun deleteComment(commentId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Delete
                path("api/v1/post/comment/$commentId")
            }
        }
    
    override suspend fun deletePost(postId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Delete
                path("api/v1/post/$postId")
            }
        }
    
    override suspend fun blockPost(postId: String): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/$postId/block")
            }
        }
}