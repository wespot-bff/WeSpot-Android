package com.bff.wespot

import androidx.navigation.NavController
import com.bff.wespot.message.screen.MessageScreenArgs
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.bff.wespot.message.screen.destinations.ReceiverSelectionScreenDestination
import com.bff.wespot.message.screen.send.ReceiverSelectionScreenArgs
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.notification.screen.NotificationNavigator
import com.bff.wespot.vote.screen.VoteResultScreenArgs
import com.bff.wespot.vote.screen.destinations.VoteResultScreenDestination
import com.bff.wespot.vote.screen.destinations.VoteStorageScreenDestination
import com.bff.wespot.vote.screen.destinations.VotingScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate

class NotificationNavigatorImpl(private val navController: NavController): NotificationNavigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToReceiverSelectionScreen() {
        navController.navigate(
            ReceiverSelectionScreenDestination(
                ReceiverSelectionScreenArgs(false),
            ) within AppNavGraphs.message
        )
    }

    override fun navigateToMessageScreen(
        messageId: Int,
        type: NotificationType
    ) {
        navController.navigate(
            MessageScreenDestination(
                MessageScreenArgs(type = type, messageId = messageId),
            ) within AppNavGraphs.message
        )
    }

    override fun navigateToVotingScreen() {
        navController.navigate(VotingScreenDestination within AppNavGraphs.vote)
    }

    override fun navigateToVoteResultScreen(
        isNavigateFromNotification: Boolean,
        isTodayVoteResult: Boolean,
    ) {
        navController.navigate(
            VoteResultScreenDestination(
                VoteResultScreenArgs(
                    isVoting = false,
                    isNavigateFromNotification = isNavigateFromNotification,
                    isTodayVoteResult = isTodayVoteResult,
                )
            ) within AppNavGraphs.vote
        )
    }

    override fun navigateToVoteStorageScreen() {
        navController.navigate(VoteStorageScreenDestination within AppNavGraphs.vote)
    }
}