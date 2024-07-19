package com.bff.wespot.auth.state

import com.bff.wespot.model.auth.response.School

sealed class AuthAction {
    data class OnSchoolSearchChanged(val text: String) : AuthAction()

    data class OnSchoolSelected(val school: School) : AuthAction()

    data class OnGradeChanged(val grade: Int) : AuthAction()

    data class OnGradeBottomSheetChanged(val isOpen: Boolean) : AuthAction()

    data class OnClassNumberChanged(val number: Int) : AuthAction()

    data class OnGenderChanged(val gender: String) : AuthAction()

    data class OnNameChanged(val name: String) : AuthAction()

    data object LoginWithKakao : AuthAction()

    data object Signup : AuthAction()

    data object AutoLogin : AuthAction()

    data object OnStartSchoolScreen : AuthAction()

    data class OnConsentChanged(val checks: List<Boolean>) : AuthAction()

    data class Navigation(val navigate: NavigationAction) : AuthAction()
}

sealed interface NavigationAction {
    data object PopBackStack : NavigationAction
    data class NavigateToGradeScreen(val edit: Boolean) : NavigationAction
    data class NavigateToSchoolScreen(val edit: Boolean) : NavigationAction
    data class NavigateToClassScreen(val edit: Boolean) : NavigationAction
    data class NavigateToGenderScreen(val edit: Boolean) : NavigationAction
    data class NavigateToNameScreen(val edit: Boolean) : NavigationAction
    data object NavigateToEditScreen : NavigationAction
    data object NavigateToCompleteScreen : NavigationAction
}
