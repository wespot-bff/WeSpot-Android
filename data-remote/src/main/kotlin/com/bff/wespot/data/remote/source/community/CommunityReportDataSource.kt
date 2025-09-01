package com.bff.wespot.data.remote.source.community

import com.bff.wespot.data.remote.model.common.ReportReasonDto

interface CommunityReportDataSource {
    suspend fun getReportReasons(): Result<List<ReportReasonDto>>
    suspend fun reportPost(postId: String, reasonIds: List<Long>, customReason: String?, customReasonId: Long?): Result<Unit>
    suspend fun reportComment(commentId: String, reasonIds: List<Long>, customReason: String?, customReasonId: Long?): Result<Unit>
}