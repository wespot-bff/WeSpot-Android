package com.bff.wespot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.common.util.AppVersionUtils.VersionCompareResult
import com.bff.wespot.common.util.AppVersionUtils.versionCompare
import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.usecase.CacheProfileUseCase
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.model.VersionUpdateType
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.state.MainAction
import com.bff.wespot.state.MainSideEffect
import com.bff.wespot.state.MainUiState
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
import java.time.LocalDate
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
            is MainAction.OnMainScreenEntered -> handleMainScreenEntered(action.appVersionName)
            is MainAction.OnEnteredByPushNotification -> {
                handleEnteredByPushNotification(
                    type = action.type,
                    targetId = action.targetId,
                    date = action.date,
                    appVersion = action.appVersion,
                )
                trackPushNotificationClicked(
                    type = action.type,
                    userId = action.userId,
                    targetId = action.targetId,
                )
            }
            is MainAction.OnNotificationSet -> handleNotificationSet(action.isEnableNotification)
            is MainAction.OnFeatureOverviewDialogDismiss -> {
                intent { postSideEffect(MainSideEffect.DismissFeatureOverviewDialog) }
            }
            is MainAction.OnFeatureOverviewDialogNavigate -> {
                intent {
                    postSideEffect(MainSideEffect.DismissFeatureOverviewDialog)
                    postSideEffect(MainSideEffect.NavigateToDeepLink(action.deepLink))
                }
            }
        }
    }

    private fun handleMainScreenEntered(appVersion: String) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            cacheProfileUseCase()

            checkAppVersionWithLatestVersion(appVersion)

            commonRepository.getRestriction()
                .onSuccess {
                    reduce {
                        state.copy(restriction = it)
                    }
                }
        }
    }

    private suspend fun checkAppVersionWithLatestVersion(appVersion: String) = intent {
        val latestVersion = remoteConfigRepository.fetchFromRemoteConfig(RemoteConfigKey.LATEST_VERSION)

        val result = versionCompare(appVersion = appVersion, compareVersion = latestVersion)
        when (result) {
            VersionCompareResult.MAJOR_VERSION_UPDATE -> {
                if (isVersionMatchCachedVersion(latestVersion).not()) {
                    reduce { state.copy(versionUpdateType = VersionUpdateType.NEW_FEATURE_ADDED)}
                    postSideEffect(MainSideEffect.ShowVersionUpdateDialog)
                }
            }

            VersionCompareResult.MINOR_VERSION_UPDATE -> {
                if (isVersionMatchCachedVersion(latestVersion).not()) {
                    /** 마이너 버전 업데이트의 경우 업데이트 타입에 따라 다이얼로그를 구분한다. */
                    val versionUpdateTypeString = remoteConfigRepository
                        .fetchFromRemoteConfig(RemoteConfigKey.VERSION_UPDATE_TYPE)
                    val versionUpdateType = VersionUpdateType.convertVersionUpdateType(versionUpdateTypeString)

                    reduce { state.copy(versionUpdateType = versionUpdateType)}
                    postSideEffect(MainSideEffect.ShowVersionUpdateDialog)
                }
            }

            VersionCompareResult.LATEST_VERSION, VersionCompareResult.PATCH_VERSION_UPDATE -> {
            }
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
            userRepository.setFeatureNotificationSetting(isEnableNotification)
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleEnteredByPushNotification(
        type: NotificationType,
        targetId: Int,
        date: String,
        appVersion: String,
    ) = intent {
        reduce { state.copy(notificationType = type) }

        when (type) {
            NotificationType.MESSAGE -> {
                postSideEffect(MainSideEffect.NavigateToReceiverSelectionScreen)
            }
            NotificationType.MESSAGE_SENT, NotificationType.MESSAGE_RECEIVED -> {
                postSideEffect(MainSideEffect.NavigateToMessageScreen(type, targetId))
            }
            NotificationType.VOTE -> {
                postSideEffect(MainSideEffect.NavigateToVotingScreen)
            }
            NotificationType.VOTE_RESULT -> {
                val voteResultDate = date.toLocalDateFromDashPattern()
                val isTodayVoteResult = LocalDate.now().equals(voteResultDate)
                postSideEffect(MainSideEffect.NavigateToVoteResultScreen(isTodayVoteResult))
            }
            NotificationType.VOTE_RECEIVED -> {
                postSideEffect(MainSideEffect.NavigateToVoteStorageScreen)
            }
            NotificationType.PROFILE_UPDATE -> {
                /** 한번 업데이트 유도 모달을 노출 한 경우, 이후 앱 진입에서 업데이트 유도 모달 노출을 방지한다. */
                dataStoreRepository.saveString(DataStoreKey.VERSION_LAST_CHECKED, appVersion)
                postSideEffect(MainSideEffect.ShowFeatureOverviewDialog)
            }
            NotificationType.UPDATE_REQUIRED -> {
                reduce { state.copy(versionUpdateType = VersionUpdateType.NEW_FEATURE_ADDED)}
                postSideEffect(MainSideEffect.ShowVersionUpdateDialog)
            }
            NotificationType.IDLE -> { }
        }
    }

    private fun trackPushNotificationClicked(
        type: NotificationType,
        userId: String,
        targetId: Int,
    ) {
        viewModelScope.launch(coroutineDispatcher) {
            analyticsHelper.updateUserId(userId)
            analyticsHelper.logEvent(
                event = AnalyticsEvent(
                    type = "push_notification_clicked",
                    extras = listOf(
                        AnalyticsEvent.Param("targetId", targetId.toString()),
                        AnalyticsEvent.Param("type", type.name),
                    )
                )
            )
        }
    }
}
