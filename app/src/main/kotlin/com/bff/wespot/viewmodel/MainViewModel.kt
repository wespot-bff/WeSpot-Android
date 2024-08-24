package com.bff.wespot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.usecase.CacheProfileUseCase
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.user.response.NotificationSetting
import com.bff.wespot.state.MainAction
import com.bff.wespot.state.MainSideEffect
import com.bff.wespot.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
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
) : ViewModel(), ContainerHost<MainUiState, MainSideEffect> {
    override val container = container<MainUiState, MainSideEffect>(MainUiState())

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
            MainAction.OnMainScreenEntered -> handleMainScreenEntered()
            MainAction.OnNavigateByPushNotification -> handleNavigateByPushNotification()
        }
    }

    private fun handleMainScreenEntered() {
        viewModelScope.launch(coroutineDispatcher) {
            cacheProfileUseCase()
            getNotificationSettingStatus()
        }
    }

    private fun getNotificationSettingStatus() {
        viewModelScope.launch(coroutineDispatcher) {
            val isSetUp = dataStoreRepository
                .getBoolean(DataStoreKey.IS_NOTIFICATION_SET_UP)
                .first()

            if (isSetUp) {

            }
        }
    }

    private fun postNotificationSetting() = intent {
        val updatedNotificationSetting = NotificationSetting(
            isEnableVoteNotification = state.isEnableVoteNotification,
            isEnableMessageNotification = state.isEnableMessageNotification,
            isEnableMarketingNotification = state.isEnableMarketingNotification,
        )

        if (updatedNotificationSetting != state.initialNotificationSetting) {
            viewModelScope.launch {
                userRepository.updateNotificationSetting(updatedNotificationSetting)
                    .onFailure {
                        Timber.e(it)
                    }
            }
        }
    }

    private fun handleNavigateByPushNotification() = intent {
        reduce { state.copy(isPushNotificationNavigation = false) }
    }
}
