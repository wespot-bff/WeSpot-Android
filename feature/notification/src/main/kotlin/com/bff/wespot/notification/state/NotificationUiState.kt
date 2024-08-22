package com.bff.wespot.notification.state

import androidx.paging.PagingData
import com.bff.wespot.model.notification.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class NotificationUiState(
    val notificationList: Flow<PagingData<Notification>> = flow { },
    val isSendAllowed: Boolean = false,
)
