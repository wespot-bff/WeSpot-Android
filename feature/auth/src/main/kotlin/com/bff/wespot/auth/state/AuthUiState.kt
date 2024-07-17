package com.bff.wespot.auth.state

import com.bff.wespot.model.auth.response.School

data class AuthUiState(
    val schoolName: String = "",
    val schoolList: List<School> = emptyList(),
    val selectedSchool: School? = null,
    val grade: Int = -1,
    val gradeBottomSheet: Boolean = true,
    val classNumber: Int = -1,
    val gender: String = "",
    val name: String = "",
    val loading: Boolean = false,
)
