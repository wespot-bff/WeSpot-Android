package com.bff.wespot.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.AuthSideEffect
import com.bff.wespot.auth.state.AuthUiState
import com.bff.wespot.auth.state.NavigationAction
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.usecase.KakaoLoginUseCase
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.auth.request.SignUp
import com.bff.wespot.model.auth.response.School
import com.bff.wespot.model.constants.LoginState
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
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel(), ContainerHost<AuthUiState, AuthSideEffect> {
    override val container = container<AuthUiState, AuthSideEffect>(AuthUiState())

    private val loginStateP: MutableLiveData<LoginState> = MutableLiveData()
    val loginState: LiveData<LoginState> = loginStateP

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
            is AuthAction.LoginWithKakao -> loginWithKakao()
            is AuthAction.signUp -> signUp()
            is AuthAction.autoLogin -> autoLogin()
            else -> {}
        }
    }

    private fun loginWithKakao() = intent {
        viewModelScope.launch {
            runCatching {
                kakaoLoginUseCase.invoke()
            }
                .onSuccess {
                    postSideEffect(AuthSideEffect.NavigateToSchoolScreen(false))
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun autoLogin() {
        viewModelScope.launch {
            dataStoreRepository.getString(DataStoreKey.ACCESS_TOKEN)
                .collect {
                    if (it.isNotEmpty()) {
                        loginStateP.postValue(LoginState.LOGIN_SUCCESS)
                    }
                }
        }
    }

    private fun signUp() = intent {
        reduce {
            state.copy(
                loading = true,
            )
        }
        viewModelScope.launch(dispatcher) {
            val result = authRepository.signUp(
                SignUp(
                    schoolId = state.selectedSchool!!.id,
                    grade = state.grade,
                    group = state.classNumber,
                    gender = state.gender,
                ),
            )

            reduce {
                state.copy(
                    loading = false,
                )
            }

            if (result) {
                postSideEffect(AuthSideEffect.NavigateToMainActivity)
            }
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
