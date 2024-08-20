package com.bff.wespot.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.notification.NotificationRepository
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.notification.Notification
import com.bff.wespot.notification.state.NotificationAction
import com.bff.wespot.notification.state.NotificationSideEffect
import com.bff.wespot.notification.state.NotificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val notificationListRepository: BasePagingRepository<Notification, Paging<Notification>>,
) : ViewModel(), ContainerHost<NotificationUiState, NotificationSideEffect> {
    override val container =
        container<NotificationUiState, NotificationSideEffect>(NotificationUiState())

    fun onActon(action: NotificationAction) {
        when (action) {
            NotificationAction.OnNotificationScreenEntered -> getNotificationList()
            is NotificationAction.OnNotificationClicked -> handleNotificationClicked(
                action.notification,
            )
        }
    }

    private fun getNotificationList() = intent {
        viewModelScope.launch {
            runCatching {
                val result = notificationListRepository.fetchResultStream().cachedIn(viewModelScope)
                reduce { state.copy(notificationList = result) }
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private fun handleNotificationClicked(notification: Notification) {
        updateNotificationReadStatus(notification.id)
    }

    private fun updateNotificationReadStatus(id: Int) = intent {
        viewModelScope.launch {
            notificationRepository.updateNotificationReadStatus(id)
                .onSuccess {
                    getNotificationList()
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
