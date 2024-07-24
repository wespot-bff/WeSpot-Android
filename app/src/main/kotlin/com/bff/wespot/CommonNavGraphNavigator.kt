package com.bff.wespot

import androidx.navigation.NavController
import com.bff.wespot.entire.screen.EntireNavigator
import com.bff.wespot.message.screen.MessageNavigator
import com.bff.wespot.vote.screen.VoteNavigator
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
    EntireNavigator,
    VotingNavigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToVotingScreen() {
        navController.navigate(VotingScreenDestination within navGraph)
    }

    override fun navigateToResultScreen(args: VoteResultScreenArgs) {
        navController.navigate(VoteResultScreenDestination(args) within navGraph)
    }
}
