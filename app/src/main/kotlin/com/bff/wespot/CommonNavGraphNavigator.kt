package com.bff.wespot

import androidx.navigation.NavController
import com.bff.wespot.entire.screen.EntireNavigator
import com.bff.wespot.message.screen.MessageNavigator
import com.bff.wespot.message.screen.MessageScreenArgs
import com.bff.wespot.message.screen.destinations.MessageEditScreenDestination
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.bff.wespot.message.screen.destinations.MessageWriteScreenDestination
import com.bff.wespot.message.screen.destinations.ReceiverSelectionScreenDestination
import com.bff.wespot.message.screen.send.MessageEditNavigator
import com.bff.wespot.message.screen.send.MessageWriteNavigator
import com.bff.wespot.message.screen.send.MessageWriteScreenArgs
import com.bff.wespot.message.screen.send.ReceiverSelectionNavigator
import com.bff.wespot.message.screen.send.ReceiverSelectionScreenArgs
import com.bff.wespot.vote.screen.VoteNavigator
import com.bff.wespot.vote.screen.VoteResultNavigator
import com.bff.wespot.vote.screen.VoteResultScreenArgs
import com.bff.wespot.vote.screen.VotingNavigator
import com.bff.wespot.vote.screen.destinations.VoteResultScreenDestination
import com.bff.wespot.vote.screen.destinations.VotingScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec

class CommonNavGraphNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController,
) : VoteNavigator,
    MessageNavigator,
    ReceiverSelectionNavigator,
    MessageWriteNavigator,
    MessageEditNavigator,
    EntireNavigator,
    VotingNavigator,
    VoteResultNavigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToVotingScreen() {
        navController.navigate(VotingScreenDestination within navGraph)
    }

    override fun navigateReceiverSelectionScreen(args: ReceiverSelectionScreenArgs) {
        navController.navigate(ReceiverSelectionScreenDestination(args) within navGraph)
    }

    override fun navigateMessageWriteScreen(args: MessageWriteScreenArgs) {
        navController.navigate(MessageWriteScreenDestination(args) within navGraph)
    }

    override fun navigateMessageScreen(args: MessageScreenArgs) {
        navController.navigate(MessageScreenDestination(args) within navGraph)
    }

    override fun navigateMessageEditScreen() {
        navController.navigate(MessageEditScreenDestination within navGraph)
    }

    override fun navigateStorageScreen() {
    }

    override fun navigateNotificationScreen() {
    }

    override fun navigateToResultScreen(args: VoteResultScreenArgs) {
        navController.navigate(VoteResultScreenDestination(args) within navGraph)
    }

    override fun navigateToVoteHome() {
        navController.popBackStack(navGraph.startRoute.route, inclusive = false, saveState = true)
    }
}
