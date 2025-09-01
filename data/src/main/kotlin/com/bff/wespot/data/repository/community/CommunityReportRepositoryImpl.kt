package com.bff.wespot.data.repository.community

import com.bff.wespot.data.remote.source.community.CommunityReportDataSource
import com.bff.wespot.domain.repository.community.CommunityReportRepository
import com.bff.wespot.model.community.ReportReason
import javax.inject.Inject

class CommunityReportRepositoryImpl @Inject constructor(
    private val communityReportDataSource: CommunityReportDataSource
) : CommunityReportRepository {
    override suspend fun getReportReasons(): Result<List<ReportReason>> =
        communityReportDataSource.getReportReasons()
            .mapCatching { reasons ->
                reasons.map { it.toDomain() }
            }

    override suspend fun reportPost(postId: String, reasonIds: List<Long>, customReason: String?, customReasonId: Long?): Result<Unit> =
        communityReportDataSource.reportPost(postId, reasonIds, customReason, customReasonId)

    override suspend fun reportComment(commentId: String, reasonIds: List<Long>, customReason: String?, customReasonId: Long?): Result<Unit> =
        communityReportDataSource.reportComment(commentId, reasonIds, customReason, customReasonId)
}