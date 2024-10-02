package com.bff.wespot.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.AuthSideEffect
import com.bff.wespot.auth.state.AuthUiState
import com.bff.wespot.auth.state.NavigationAction
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.auth.AuthRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.usecase.AutoLoginUseCase
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.domain.usecase.KakaoLoginUseCase
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.model.SideEffect.Companion.toToastEffect
import com.bff.wespot.model.auth.request.KakaoAuthToken
import com.bff.wespot.model.auth.request.SignUp
import com.bff.wespot.model.auth.response.Consents
import com.bff.wespot.model.auth.response.School
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.constants.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val autoLoginUseCase: AutoLoginUseCase,
    private val checkProfanityUseCase: CheckProfanityUseCase,
    private val pagingRepository: BasePagingRepository<School, Paging<School>>,
    remoteConfigRepository: RemoteConfigRepository,
) : BaseViewModel(), ContainerHost<AuthUiState, AuthSideEffect> {
    override val container = container<AuthUiState, AuthSideEffect>(
        AuthUiState(
            playStoreLink =
                remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.PLAY_STORE_URL),
            termsOfServiceLink =
                remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.TERMS_OF_SERVICE_URL),
            privacyPolicyLink =
                remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.PRIVACY_POLICY_URL),
            schoolForm = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.SCHOOL_FORM),
            marketingLink =
                remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.MARKETING_SERVICE_TERM),
        ),
    )

    private val loginStateP: MutableLiveData<LoginState> = MutableLiveData()
    val loginState: LiveData<LoginState> = loginStateP

    private val userInput = MutableStateFlow("")
    private val nameInput = MutableStateFlow("")

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
            is AuthAction.LoginWithKakao -> loginWithKakao(action.token)
            is AuthAction.Signup -> signUp()
            is AuthAction.AutoLogin -> autoLogin(action.versionCode)
            is AuthAction.OnStartSchoolScreen -> monitorUserInput()
            is AuthAction.OnStartNameScreen -> monitorNameInput()
            is AuthAction.OnConsentChanged -> handleConsentChanged(action.checks)
        }
    }

    private fun loginWithKakao(kakaoAuthToken: KakaoAuthToken) = intent {
        viewModelScope.launch {
            try {
                kakaoLoginUseCase(kakaoAuthToken)
                    .onSuccess {
                        if (it == LoginState.LOGIN_SUCCESS) {
                            postSideEffect(AuthSideEffect.NavigateToMainActivity)
                        } else {
                            postSideEffect(AuthSideEffect.NavigateToSchoolScreen(false))
                        }
                    }
                    .onNetworkFailure {
                        postSideEffect(it.toSideEffect())
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun autoLogin(versionCode: String) {
        viewModelScope.launch {
            autoLoginUseCase(versionCode).let {
                loginStateP.postValue(it)
            }
        }
    }

    private fun signUp() = intent {
        reduce {
            state.copy(
                loading = true,
            )
        }
        viewModelScope.launch(coroutineDispatcher) {
            val result = authRepository.signUp(
                SignUp(
                    name = state.name,
                    schoolId = state.selectedSchool!!.id,
                    grade = state.grade,
                    classNumber = state.classNumber,
                    gender = state.gender,
                    consents = Consents(
                        marketing = state.consents[3],
                    ),
                ),
            )

            reduce {
                state.copy(
                    loading = false,
                )
            }

            if (result) {
                postSideEffect(AuthSideEffect.NavigateToMainActivity)
            } else {
                postSideEffect(toToastEffect())
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
        viewModelScope.launch(coroutineDispatcher) {
            userInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect {
                    fetchSchoolList(it)
                }
        }
    }

    private fun monitorNameInput() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            nameInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect {
                    runCatching {
                        val result = checkProfanityUseCase(it)
                        reduce {
                            state.copy(
                                hasProfanity = result,
                            )
                        }
                    }
                }
        }
    }

    private fun fetchSchoolList(search: String) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                val result = pagingRepository.fetchResultStream(mapOf("search" to search))
                reduce {
                    state.copy(
                        schoolList = result,
                    )
                }
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
        nameInput.value = name
        reduce {
            state.copy(
                name = name,
            )
        }
    }

    private fun handleConsentChanged(checks: List<Boolean>) = intent {
        reduce {
            state.copy(
                consents = checks,
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
