package com.bff.wespot.navigation

interface NotificationNavigator {
    fun navigateUp()
    fun navigateToReceiverSelectionScreen()
    fun navigateToVotingScreen()
    fun navigateToVoteResultScreen(
        isNavigateFromNotification: Boolean,
        isTodayVoteResult: Boolean,
    )
    fun navigateToVoteStorageScreen()
    fun navigateToProfileEditScreen()
}
