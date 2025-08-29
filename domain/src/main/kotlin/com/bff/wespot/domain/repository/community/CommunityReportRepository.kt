package com.bff.wespot.domain.repository.community

import com.bff.wespot.model.community.ReportReason

interface CommunityReportRepository {
    suspend fun getReportReasons(): Result<List<ReportReason>>
    suspend fun reportPost(postId: String, reasonIds: List<Long>): Result<Unit>
    suspend fun reportComment(commentId: String, reasonIds: List<Long>): Result<Unit>
}
