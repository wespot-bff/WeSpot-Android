package com.bff.wespot.state

import com.bff.wespot.model.notification.NotificationType

sealed interface MainSideEffect {
    data object NavigateToVoteStorageScreen: MainSideEffect
    data object NavigateToReceiverSelectionScreen: MainSideEffect
    data object NavigateToVotingScreen: MainSideEffect
    data class NavigateToMessageScreen(val type: NotificationType, val messageId: Int): MainSideEffect
    data class NavigateToVoteResultScreen(val isTodayVoteResult: Boolean): MainSideEffect
    data class NavigateToDeepLink(val deepLink: String): MainSideEffect
}
