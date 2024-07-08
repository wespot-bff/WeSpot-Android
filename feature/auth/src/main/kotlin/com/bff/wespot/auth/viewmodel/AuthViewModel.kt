package com.bff.wespot.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.AuthSideEffect
import com.bff.wespot.auth.state.AuthUiState
import com.bff.wespot.model.SchoolItem
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class AuthViewModel: ViewModel() , ContainerHost<AuthUiState, AuthSideEffect> {
    override val container = container<AuthUiState, AuthSideEffect>(AuthUiState())

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnSchoolSearchChanged -> handleSchoolSearchChanged(action.text)
            is AuthAction.OnSchoolSelected -> handleSchoolSelected(action.school)
            else -> {}
        }
    }

    private fun handleSchoolSearchChanged(text: String) = intent {
        reduce {
            state.copy(
                schoolName = text,
                schoolSearchList = state.schoolList.filter { it.name.contains(text, ignoreCase = true) }
            )
        }
    }

    private fun handleSchoolSelected(school: SchoolItem) = intent {
        reduce {
            state.copy(
                selectedSchool = school
            )
        }
    }
}