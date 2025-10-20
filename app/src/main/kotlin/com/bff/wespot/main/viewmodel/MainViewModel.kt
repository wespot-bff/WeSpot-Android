package com.bff.wespot.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.util.AppVersionUtils.VersionCompareResult
import com.bff.wespot.common.util.AppVersionUtils.versionCompare
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.usecase.CacheProfileUseCase
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.main.model.VersionUpdateType
import com.bff.wespot.main.state.MainAction
import com.bff.wespot.main.state.MainSideEffect
import com.bff.wespot.main.state.MainUiState
import com.bff.wespot.model.notification.PushNotificationData
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cacheProfileUseCase: CacheProfileUseCase,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val commonRepository: CommonRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel(),
    ContainerHost<MainUiState, MainSideEffect> {
    override val container = container<MainUiState, MainSideEffect>(
        MainUiState(
            kakaoChannel = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.WESPOT_KAKAO_CHANNEL_URL,
            ),
            playStoreLink = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.PLAY_STORE_URL,
            ),
        ),
    )

    init {
        viewModelScope.launch {
            dataStoreRepository.getString(DataStoreKey.ID).collect {
                intent {
                    if (it.isNotEmpty()) {
                        reduce {
                            state.copy(userId = it)
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.OnMainScreenEntered -> {
                handleMainScreenEntered(action.appVersionName)
            }
            is MainAction.OnEnteredByPushNotification -> {
                handleEnteredFromPushNotification(action.data)
            }
            is MainAction.OnNotificationSet -> handleNotificationSet(action.isEnableNotification)
            is MainAction.CloseOnBoarding -> handleOnBoardingClose(action.category)
        }
    }

    private fun handleMainScreenEntered(appVersion: String) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            cacheProfileUseCase()

            checkAppVersionWithLatestVersion(appVersion)

            commonRepository
                .getRestriction()
                .onSuccess {
                    reduce {
                        state.copy(restriction = it)
                    }
                }
        }
    }

    /** RemoteConfig에서 최신 버전을 가져와 현재 앱 버전과 비교하고 적절한 업데이트 다이얼로그를 노출한다.*/
    private fun checkAppVersionWithLatestVersion(appVersion: String) = intent {
        val latestVersion = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.LATEST_VERSION)

        val result = versionCompare(appVersion = appVersion, compareVersion = latestVersion)
        when (result) {
            VersionCompareResult.MAJOR_VERSION_UPDATE -> {
                if (isVersionMatchCachedVersion(latestVersion).not()) {
                    postSideEffect(
                        MainSideEffect.ShowVersionUpdateDialog(VersionUpdateType.NEW_FEATURE_ADDED),
                    )
                }
            }

            VersionCompareResult.MINOR_VERSION_UPDATE -> {
                if (isVersionMatchCachedVersion(latestVersion).not()) {
                    /** 마이너 버전 업데이트의 경우 업데이트 타입에 따라 다이얼로그를 구분한다. */
                    val versionUpdateTypeString = remoteConfigRepository
                        .fetchFromRemoteConfig(RemoteConfigKey.VERSION_UPDATE_TYPE)
                    val versionUpdateType = VersionUpdateType.convertVersionUpdateType(versionUpdateTypeString)

                    postSideEffect(MainSideEffect.ShowVersionUpdateDialog(versionUpdateType))
                }
            }

            VersionCompareResult.LATEST_VERSION, VersionCompareResult.PATCH_VERSION_UPDATE -> {}
        }
    }

    /**
     * 캐시된 버전 정보를 확인하여 이전에 비교한 경험이 있는지 확인한다.
     * 만약 새로운 최신 버전이라면 캐시에 저장하고 false를 반환한다.
     */
    private suspend fun isVersionMatchCachedVersion(version: String): Boolean {
        val versionLastChecked = dataStoreRepository
            .getString(DataStoreKey.VERSION_LAST_CHECKED)
            .firstOrNull()

        if (version != versionLastChecked) {
            dataStoreRepository.saveString(DataStoreKey.VERSION_LAST_CHECKED, version)
            return false
        } else {
            return true
        }
    }

    private fun handleNotificationSet(isEnableNotification: Boolean) = intent {
        viewModelScope.launch {
            userRepository
                .setFeatureNotificationSetting(isEnableNotification)
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleEnteredFromPushNotification(data: PushNotificationData) = intent {
        postSideEffect(MainSideEffect.NavigateFromPushNotification(data))
    }

    private fun handleOnBoardingClose(category: OnBoardingCategory) = intent {
        when (category) {
            OnBoardingCategory.VOTE -> reduce { state.copy(showVoteOnBoarding = false) }
            OnBoardingCategory.MESSAGE -> reduce { state.copy(showMessageOnBoarding = false) }
        }
    }
}
