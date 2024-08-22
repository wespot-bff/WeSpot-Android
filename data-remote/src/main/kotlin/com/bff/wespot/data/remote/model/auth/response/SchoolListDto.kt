package com.bff.wespot.data.remote.model.auth.response

import com.bff.wespot.model.auth.response.SchoolList
import kotlinx.serialization.Serializable

@Serializable
data class SchoolListDto(
    val schools: List<SchoolDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toSchoolList(): SchoolList = SchoolList(
        data = schools.map { it.toSchool() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )
}