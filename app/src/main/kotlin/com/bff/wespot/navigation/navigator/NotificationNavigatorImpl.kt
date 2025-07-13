package com.bff.wespot.navigation.navigator

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.entire.screen.destinations.ProfileEditScreenDestination
import com.bff.wespot.message.screen.destinations.ReceiverSelectionScreenDestination
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.notification.PushNotificationData
import com.bff.wespot.navigation.AppNavGraphs
import com.bff.wespot.navigation.NotificationNavigator
import com.bff.wespot.vote.screen.VoteResultScreenArgs
import com.bff.wespot.vote.screen.destinations.VoteResultScreenDestination
import com.bff.wespot.vote.screen.destinations.VoteStorageScreenDestination
import com.bff.wespot.vote.screen.destinations.VotingScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import java.time.LocalDate

class NotificationNavigatorImpl(
    private val navController: NavController,
) : NotificationNavigator {
    override fun navigate(context: Context, data: PushNotificationData) {
        when (data.type) {
            NotificationType.MESSAGE -> navigateToReceiverSelectionScreen()
            NotificationType.MESSAGE_V2 -> {
                val intent = Intent(Intent.ACTION_VIEW, data.deepLink.toUri())
                context.startActivity(intent)
            }
            NotificationType.VOTE -> navigateToVotingScreen()
            NotificationType.VOTE_RESULT -> {
                val voteResultDate = data.date.toLocalDateFromDashPattern()
                val isTodayVoteResult = LocalDate.now().equals(voteResultDate)
                navigateToVoteResultScreen(
                    isNavigateFromNotification = false,
                    isTodayVoteResult = isTodayVoteResult,
                )
            }
            NotificationType.VOTE_RECEIVED -> navigateToVoteStorageScreen()
            NotificationType.PROFILE_UPDATE -> navigateToProfileEditScreen()
            NotificationType.IDLE -> {}
        }
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToReceiverSelectionScreen() {
        navController.navigate(
            ReceiverSelectionScreenDestination() within AppNavGraphs.message,
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
                ),
            ) within AppNavGraphs.vote,
        )
    }

    override fun navigateToVoteStorageScreen() {
        navController.navigate(VoteStorageScreenDestination within AppNavGraphs.vote)
    }

    override fun navigateToProfileEditScreen() {
        navController.navigate(ProfileEditScreenDestination within AppNavGraphs.entire)
    }
}
