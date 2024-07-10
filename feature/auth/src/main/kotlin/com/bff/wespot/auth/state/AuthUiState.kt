package com.bff.wespot.auth.state

import com.bff.wespot.model.SchoolItem

data class AuthUiState(
    val schoolName: String = "",
    val schoolList: List<SchoolItem> = listOf(SchoolItem("1", "2", "3")),
    val schoolSearchList: List<SchoolItem> = listOf(SchoolItem("2", "2", "3")),
    val selectedSchool: SchoolItem? = null,
    val grade: Int = -1,
    val gradeBottomSheet: Boolean = true,
    val classNumber: Int = -1,
    val gender: String = "",
    val name: String = "",
)
