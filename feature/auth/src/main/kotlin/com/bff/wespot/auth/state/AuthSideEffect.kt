package com.bff.wespot.auth.state

sealed class AuthSideEffect {
    data object PopBackStack : AuthSideEffect()
    data class NavigateToGradeScreen(val edit: Boolean) : AuthSideEffect()
    data class NavigateToSchoolScreen(val edit: Boolean) : AuthSideEffect()
    data class NavigateToClassScreen(val edit: Boolean) : AuthSideEffect()
    data class NavigateToGenderScreen(val edit: Boolean) : AuthSideEffect()
    data class NavigateToNameScreen(val edit: Boolean) : AuthSideEffect()
    data object NavigateToEditScreen : AuthSideEffect()
    data object NavigateToCompleteScreen : AuthSideEffect()
}
