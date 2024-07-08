package com.bff.wespot.auth.state

import com.bff.wespot.model.SchoolItem

data class AuthUiState(
    val schoolName: String = "",
    val schoolList: List<SchoolItem> = emptyList(),
    val schoolSearchList: List<SchoolItem> = emptyList(),
    val selectedSchool: SchoolItem? = null,
    val grade: Int = -1,
    val gradeBottomSheet: Boolean = true,
)
