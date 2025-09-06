package com.bff.wespot.domain.repository.community

import com.bff.wespot.model.community.ReportReason
import com.bff.wespot.model.community.ReportReasonItem

interface CommunityReportRepository {
    suspend fun getReportReasons(): Result<List<ReportReason>>
    suspend fun reportPost(
        postId: String,
        reportItems: List<ReportReasonItem>,
    ): Result<Unit>

    suspend fun reportComment(
        commentId: String,
        reportItems: List<ReportReasonItem>,
    ): Result<Unit>
}
