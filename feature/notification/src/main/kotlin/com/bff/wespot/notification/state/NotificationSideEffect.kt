package com.bff.wespot.notification.state

import com.bff.wespot.ui.model.ToastState

sealed interface NotificationSideEffect {
    data class NavigateByDeepLink(val deepLink: String) : NotificationSideEffect
    data object NavigateToReceiverSelectionScreen : NotificationSideEffect
    data class NavigateToVoteResultScreen(
        val isNavigateFromNotification: Boolean,
        val isTodayVoteResult: Boolean,
    ) : NotificationSideEffect
    data object NavigateToVotingScreen : NotificationSideEffect
    data object NavigateToVoteStorageScreen : NotificationSideEffect
    data object NavigateToProfileEditScreen : NotificationSideEffect
    data class ShowToast(val toastState: ToastState) : NotificationSideEffect
}
