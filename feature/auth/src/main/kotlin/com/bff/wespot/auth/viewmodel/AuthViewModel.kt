package com.bff.wespot.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.AuthSideEffect
import com.bff.wespot.auth.state.AuthUiState
import com.bff.wespot.auth.state.NavigationAction
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.usecase.KakaoLoginUseCase
import com.bff.wespot.model.auth.School
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel(), ContainerHost<AuthUiState, AuthSideEffect> {
    override val container = container<AuthUiState, AuthSideEffect>(AuthUiState())

    private val userInput = MutableStateFlow("")

    init {
        monitorUserInput()
    }

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnSchoolSearchChanged -> handleSchoolSearchChanged(action.text)
            is AuthAction.OnSchoolSelected -> handleSchoolSelected(action.school)
            is AuthAction.OnGradeBottomSheetChanged -> handleGradeBottomSheetChanged(action.isOpen)
            is AuthAction.OnGradeChanged -> handleGradeChanged(action.grade)
            is AuthAction.OnClassNumberChanged -> handleClassNumberChanged(action.number)
            is AuthAction.OnGenderChanged -> handleGenderChanged(action.gender)
            is AuthAction.OnNameChanged -> handleNameChanged(action.name)
            is AuthAction.Navigation -> handleNavigation(action.navigate)
            else -> {}
        }
    }

    fun loginWithKakao() {
        viewModelScope.launch {
            kakaoLoginUseCase.invoke()
        }
    }

    private fun handleSchoolSearchChanged(text: String) = intent {
        reduce {
            userInput.value = text
            state.copy(
                schoolName = text,
            )
        }
    }

    private fun monitorUserInput() {
        viewModelScope.launch {
            userInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect {
                    fetchSchoolList(it)
                }
        }
    }

    private fun fetchSchoolList(search: String) = intent {
        viewModelScope.launch(dispatcher) {
            Timber.d("enter")
            authRepository.getSchoolList(search)
                .onSuccess {
                    reduce {
                        state.copy(
                            schoolList = it,
                        )
                    }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleSchoolSelected(school: School) = intent {
        reduce {
            state.copy(
                selectedSchool = school,
            )
        }
    }

    private fun handleGradeBottomSheetChanged(isOpen: Boolean) = intent {
        reduce {
            state.copy(
                gradeBottomSheet = isOpen,
            )
        }
    }

    private fun handleGradeChanged(grade: Int) = intent {
        reduce {
            state.copy(
                grade = grade,
            )
        }
    }

    private fun handleClassNumberChanged(number: Int) = intent {
        reduce {
            state.copy(
                classNumber = number,
            )
        }
    }

    private fun handleGenderChanged(gender: String) = intent {
        reduce {
            state.copy(
                gender = gender,
            )
        }
    }

    private fun handleNameChanged(name: String) = intent {
        reduce {
            state.copy(
                name = name,
            )
        }
    }

    private fun handleNavigation(navigate: NavigationAction) = intent {
        val sideEffect = when (navigate) {
            NavigationAction.PopBackStack -> AuthSideEffect.PopBackStack
            is NavigationAction.NavigateToGradeScreen -> AuthSideEffect.NavigateToGradeScreen(
                navigate.edit,
            )

            is NavigationAction.NavigateToSchoolScreen -> AuthSideEffect.NavigateToSchoolScreen(
                navigate.edit,
            )

            is NavigationAction.NavigateToClassScreen -> AuthSideEffect.NavigateToClassScreen(
                navigate.edit,
            )

            is NavigationAction.NavigateToGenderScreen -> AuthSideEffect.NavigateToGenderScreen(
                navigate.edit,
            )

            is NavigationAction.NavigateToNameScreen -> AuthSideEffect.NavigateToNameScreen(
                navigate.edit,
            )

            NavigationAction.NavigateToEditScreen -> AuthSideEffect.NavigateToEditScreen
            NavigationAction.NavigateToCompleteScreen -> AuthSideEffect.NavigateToCompleteScreen
        }
        postSideEffect(sideEffect)
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}
