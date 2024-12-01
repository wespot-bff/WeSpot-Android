package com.bff.wespot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.MainScreenNavArgs
import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.common.util.AppVersionUtils.VersionCompareResult
import com.bff.wespot.common.util.AppVersionUtils.versionCompare
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.usecase.CacheProfileUseCase
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.model.VersionUpdateType
import com.bff.wespot.state.MainAction
import com.bff.wespot.state.MainSideEffect
import com.bff.wespot.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
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
    private val analyticsHelper: AnalyticsHelper,
    private val commonRepository: CommonRepository,
    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel(), ContainerHost<MainUiState, MainSideEffect> {
    override val container = container<MainUiState, MainSideEffect>(
        MainUiState(
            kakaoChannel = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.WESPOT_KAKAO_CHANNEL_URL,
            ),
            playStoreLink = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.PLAY_STORE_URL,
            ),
        )
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
            MainAction.OnNavigateByPushNotification -> handleNavigateByPushNotification()
            is MainAction.OnMainScreenEntered -> handleMainScreenEntered(action.appVersionName)
            is MainAction.OnEnteredByPushNotification -> {
                handleEnteredByPushNotification()
                trackPushNotificationClicked(action.data)
            }
            is MainAction.OnNotificationSet -> handleNotificationSet(action.isEnableNotification)
        }
    }

    private fun handleMainScreenEntered(appVersion: String) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            cacheProfileUseCase()

            if (isAppVersionMatchCachedVersion(appVersion).not()) {
                checkAppVersionWithLatestVersion(appVersion)
            }

            commonRepository.getRestriction()
                .onSuccess {
                    reduce {
                        state.copy(restriction = it)
                    }
                }
        }
    }

    /** 마지막으로 버전 검사를 진행한 버전 정보를 불러와 현재 앱 버전과 대조한다. */
    private suspend fun isAppVersionMatchCachedVersion(appVersion: String): Boolean {
        val versionLastChecked = dataStoreRepository
            .getString(DataStoreKey.VERSION_LAST_CHECKED)
            .first()

        return appVersion == versionLastChecked
    }

    /** 최신 버전을 불러와 현재 버전과 비교하며, 현재 앱 정보를 마지막 버전 검사 버전으로 저장한다. */
    private suspend fun checkAppVersionWithLatestVersion(appVersion: String) = intent {
        val latestVersion = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.LATEST_VERSION)

        val result = versionCompare(appVersion = appVersion, compareVersion = latestVersion)
        when (result) {
            VersionCompareResult.MAJOR_VERSION_UPDATE -> {
                postSideEffect(MainSideEffect.ShowVersionUpdateDialog(VersionUpdateType.NEW_FEATURE_ADDED))
            }

            VersionCompareResult.MINOR_VERSION_UPDATE -> {
                val versionUpdateTypeString = remoteConfigRepository
                    .fetchFromRemoteConfig(RemoteConfigKey.VERSION_UPDATE_TYPE)
                val versionUpdateType = VersionUpdateType.convertVersionUpdateType(versionUpdateTypeString)
                postSideEffect(MainSideEffect.ShowVersionUpdateDialog(versionUpdateType))
            }

            VersionCompareResult.LATEST_VERSION, VersionCompareResult.PATCH_VERSION_UPDATE -> {
            }
        }

        dataStoreRepository.saveString(DataStoreKey.VERSION_LAST_CHECKED, appVersion)
    }

    private fun handleNotificationSet(isEnableNotification: Boolean) = intent {
        viewModelScope.launch {
            userRepository.setFeatureNotificationSetting(isEnableNotification)
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleEnteredByPushNotification() = intent {
        reduce { state.copy(isPushNotificationNavigation = true) }
    }

    private fun handleNavigateByPushNotification() = intent {
        reduce { state.copy(isPushNotificationNavigation = false) }
    }

    private fun trackPushNotificationClicked(data: MainScreenNavArgs) {
        viewModelScope.launch(coroutineDispatcher) {
            analyticsHelper.updateUserId(data.userId)
            analyticsHelper.logEvent(
                event = AnalyticsEvent(
                    type = "push_notification_clicked",
                    extras = listOf(
                        AnalyticsEvent.Param("targetId", data.targetId.toString()),
                        AnalyticsEvent.Param("type", data.type.name),
                    )
                )
            )
        }
    }
}
