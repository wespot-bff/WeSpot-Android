package com.bff.wespot.auth.state

import com.bff.wespot.model.SchoolItem

sealed class AuthAction {
    data class OnSchoolSearchChanged(val text: String) : AuthAction()
    data class OnSchoolSelected(val school: SchoolItem) : AuthAction()
    data class OnGradeChanged(val grade: Int) : AuthAction()
    data class OnGradeBottomSheetChanged(val isOpen: Boolean) : AuthAction()
}