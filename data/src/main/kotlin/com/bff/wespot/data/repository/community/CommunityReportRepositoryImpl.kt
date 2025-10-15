package com.bff.wespot.data.repository.community

import com.bff.wespot.data.remote.source.community.CommunityReportDataSource
import com.bff.wespot.domain.repository.community.CommunityReportRepository
import com.bff.wespot.model.community.ReportReason
import com.bff.wespot.model.community.ReportReasonItem
import javax.inject.Inject

class CommunityReportRepositoryImpl @Inject constructor(
    private val communityReportDataSource: CommunityReportDataSource
) : CommunityReportRepository {
    override suspend fun getReportReasons(): Result<List<ReportReason>> =
        communityReportDataSource.getReportReasons()
            .mapCatching { reasons ->
                reasons.map { it.toDomain() }
            }

    override suspend fun reportPost(
        postId: String,
        reportItems: List<ReportReasonItem>,
    ): Result<Unit> =
        communityReportDataSource.reportPost(postId, reportItems)

    override suspend fun reportComment(
        commentId: String,
        reportItems: List<ReportReasonItem>,
    ): Result<Unit> =
        communityReportDataSource.reportComment(commentId, reportItems)
}