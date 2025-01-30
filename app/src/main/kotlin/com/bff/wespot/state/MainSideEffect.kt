package com.bff.wespot.state

import com.bff.wespot.model.notification.NotificationType

sealed interface MainSideEffect {
    data object NavigateToVoteStorageScreen: MainSideEffect
    data object ShowFeatureOverviewDialog: MainSideEffect
    data object NavigateToReceiverSelectionScreen: MainSideEffect
    data object NavigateToVotingScreen: MainSideEffect
    data object ShowVersionUpdateDialog: MainSideEffect
    data class NavigateToMessageScreen(val type: NotificationType, val messageId: Int): MainSideEffect
    data class NavigateToVoteResultScreen(val isTodayVoteResult: Boolean): MainSideEffect
}
