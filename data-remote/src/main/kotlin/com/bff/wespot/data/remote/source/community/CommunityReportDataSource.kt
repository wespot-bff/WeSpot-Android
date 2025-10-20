package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.common.ReportReasonDto
import com.bff.wespot.model.community.ReportReasonItem

interface CommunityReportDataSource {
    suspend fun getReportReasons(): Result<List<ReportReasonDto>>
    suspend fun reportPost(postId: String, reportItems: List<ReportReasonItem>): Result<Unit>
    suspend fun reportComment(commentId: String, reportItems: List<ReportReasonItem>): Result<Unit>
}