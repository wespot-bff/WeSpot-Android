package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.common.ReportReasonDto
import com.bff.wespot.data.remote.model.community.ReportReasonItem
import com.bff.wespot.data.remote.model.community.ReportReasonRequest
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.path
import javax.inject.Inject

class CommunityReportDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : CommunityReportDataSource {
    override suspend fun getReportReasons(): Result<List<ReportReasonDto>> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/reports")
            }
        }

    override suspend fun reportPost(
        postId: String,
        reasonIds: List<Long>,
        customReason: String?,
        customReasonId: Long?
    ): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/$postId/report")
                contentType(ContentType.Application.Json)
                val reportItems = buildList {
                    addAll(
                        reasonIds.map {
                            ReportReasonItem(
                                reportReasonId = it,
                                customReason = null
                            )
                        }
                    )
                    if (!customReason.isNullOrEmpty() && customReasonId != null) {
                        add(ReportReasonItem(reportReasonId = customReasonId, customReason = customReason))
                    }
                }
                setBody(ReportReasonRequest(reportReasonRequests = reportItems))
            }
        }

    override suspend fun reportComment(
        commentId: String,
        reasonIds: List<Long>,
        customReason: String?,
        customReasonId: Long?
    ): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v1/post/comment/$commentId/report")
                contentType(ContentType.Application.Json)
                val reportItems = buildList {
                    addAll(
                        reasonIds.map {
                            ReportReasonItem(
                                reportReasonId = it,
                                customReason = null
                            )
                        }
                    )
                    if (!customReason.isNullOrEmpty() && customReasonId != null) {
                        add(ReportReasonItem(reportReasonId = customReasonId, customReason = customReason))
                    }
                }
                setBody(ReportReasonRequest(reportReasonRequests = reportItems))
            }
        }
}