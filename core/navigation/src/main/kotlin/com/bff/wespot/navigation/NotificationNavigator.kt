package com.bff.wespot.navigation

import android.content.Context
import com.bff.wespot.model.notification.PushNotificationData

interface NotificationNavigator {
    fun navigate(context: Context, data: PushNotificationData)
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
